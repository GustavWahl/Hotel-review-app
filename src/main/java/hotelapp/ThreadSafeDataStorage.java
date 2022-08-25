package hotelapp;

import customLock.ReentrantReadWriteLock;
import hotelapp.DataClasses.Hotel;
import hotelapp.DataClasses.Review;
import servers.FetchWeatherData;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeDataStorage extends DataStorage {
    private ReentrantReadWriteLock lock;




    /**
     * Constructor initialises paths, json parse class and inverted index
     *  @param hotelFilePath
     * @param reviewDirectoryPath
     * @param numThreads
     **/
    public ThreadSafeDataStorage(String hotelFilePath, String reviewDirectoryPath, int numThreads) {
        super(hotelFilePath, reviewDirectoryPath);
        this.lock = new ReentrantReadWriteLock();

        initializeHotelData(hotelFilePath);

        if (reviewDirectoryPath != null) {
            initializeReviewData(reviewDirectoryPath, numThreads);
        }


    }

    public int getReviewL(){
        return reviews.size();


    }

    public String getTot(){
        AtomicInteger count = new AtomicInteger();
        reviews.forEach( (key, value) -> {
            for (Review r: value
                 ) {
                count.getAndIncrement();
            }
        });

        return count.toString();
    }



    /** Method that initialises hotel data**/
    private void initializeHotelData(String hotelFilePath) {
        try {
            lock.lockWrite();
            super.initialiseHotelData(hotelFilePath);
        } finally {
            lock.unlockWrite();
        }
    }

    /** Method that initialises the review data and sorts the list,
     * then put the data in a HashMap
     * method also calls the createInvertedIndex method
     * **/
    private void initializeReviewData(String reviewDirectoryPath, int numThreads) {
        MultiThreadedReviewParser multiThreadedReviewParser = new MultiThreadedReviewParser(numThreads);
        reviews = new HashMap<>();
        try {
            lock.lockWrite();

            reviews = multiThreadedReviewParser.manageExecutor(reviewDirectoryPath);

            createInvertedIndex();
        } finally {
            lock.unlockWrite();
        }


    }

    /** Method that creates inverted index data structure**/
    private void createInvertedIndex(){
        try {
            lock.lockWrite();

            Map<String, Integer> frequency = new HashMap<>();
            String[] wordsToRemove = {"a", "the", "is", "are", "were", "and", ",", ".", ";", "!", "..."};


            reviews.forEach((key, val) -> {
                for (Review r : val) {
                    String text = r.getReviewText();

                    text = text.toLowerCase();

                    for (String s : wordsToRemove) {
                        if (text.contains(s)) {
                            text = text.replaceAll(s, " ");
                        }
                    }

                    for (String word : text.split(" ")) {
                        if (!invertedIndex.containsKey(word)) {
                            Set<Review> reviews = new HashSet<>();
                            reviews.add(r);
                            invertedIndex.put(word, reviews);
                        } else if (!invertedIndex.get(word).isEmpty()) {
                            invertedIndex.get(word).add(r);
                        }

                        if (!frequency.containsKey(word)) {
                            frequency.put(word, 1);
                        } else {
                            int count = frequency.get(word);
                            count++;
                            frequency.put(word, count);
                        }
                    }
                }
            });
        } finally {
            lock.unlockWrite();
        }
        //System.out.println(frequency.get("nice"));
    }



    /** Method that prints hotel data based on hotelId
     * @param hotelId
     * **/
    public boolean findHotel(String hotelId){
        try {
            lock.lockRead();

            for (Hotel h : hotels) {
                if (h.getId().equals(hotelId)) {
                    System.out.println(h.toString());
                    return true;
                }
            }
        } finally {
            lock.unlockRead();
        }
        return false;
    }

    /** Method that prints all reviews connected to the hotelId
     * @param hotelId
     * **/
    public boolean findReviews(String hotelId) {

        List<Review> rw;
        try {
            lock.lockRead();
            if ((rw = reviews.get(hotelId)) != null) {
                for (Review r : rw) {
                    System.out.println(r.toString());
                }
                return true;
            }
        } finally {
            lock.unlockRead();
        }
        return false;
    }

    /** Method that prints all reviews that includes the word
     * @param word
     * **/
    public boolean findWord(String word) {
        try {
            lock.lockRead();

            if (invertedIndex.containsKey(word)){
                Map<Integer, Set<Review>> freq = new TreeMap<>();


                Iterator<Review> reviewIterator = invertedIndex.get(word).iterator();

                while (reviewIterator.hasNext()){
                    int count = 0;
                    Review review = reviewIterator.next();

                    for (String s : review.getReviewText().split(" ")){
                        if (s.equals(word)){
                            count++;
                        }
                    }
                    if (!freq.containsKey(count)){

                        Set<Review> reviewSet = new HashSet<>();
                        reviewSet.add(review);
                        freq.put(count, reviewSet);
                    }
                    else if (!freq.get(count).isEmpty()) {
                        freq.get(count).add(review);
                    }

                }

                freq.forEach( (key, val) -> {
                    System.out.println(val.toString());
                });


                return true;
            }
        } finally {
            lock.unlockRead();
        }
        return false;
    }

    /** Method calls static method to print hotels and reviews to file
     * @param output
     * **/
    public void printToFile(String output) {
        try {
            lock.lockRead();

            JSONDataPrinter.printToFile(hotels, reviews, output);
        } finally {
            lock.unlockRead();
        }
    }

    /**
     * Method that returns json object from hotelID
     * @param id
     * @return
     */
    public String getHotelJsonObject(String id){
        try {
            lock.lockRead();

            for (Hotel h : hotels) {
                if (h.getId().equals(id)) {

                    return JSONDataParser.hotelToJson(h);
                }
            }

        } finally {
            lock.unlockRead();
        }
        return JSONDataParser.invalidId();
    }


    /**
     * Method that returns jsonobject from review from inverted index
     * @param word
     * @param num
     * @return
     */
    public String getIndexJson(String word, int num){
        try {
            lock.lockRead();

            Set<Review> reviewSet = invertedIndex.get(word);
            if (word == null || reviewSet == null || num == 0) return JSONDataParser.invalidWord();

            return JSONDataParser.reviewsFromWord(reviewSet, num, word);

        } finally {
            lock.unlockRead();
        }
    }

    /**
     * Method that returns the review json object from hotelId
     * @param id
     * @param num
     * @return
     */
    public String getReviewJsonObject(String id, int num){
        try {
            lock.lockRead();

            List<Review> reviewList = reviews.get(id);
            if (reviewList == null || num == 0) return JSONDataParser.invalidId();
            return JSONDataParser.reviewToJson(reviewList, num, id);

        } finally {
            lock.unlockRead();
        }
    }

    /**
     * Method that returns the weather json object
     * @param hotelId
     * @return
     */
    public String getWeather(String hotelId) {

        try {
            lock.lockRead();

            for (Hotel h : hotels) {
                if (h.getId().equals(hotelId)) {
                    String lon = "" + h.getLng();
                    String lat = "" + h.getLat();

                    String jsonData = FetchWeatherData.fetchData(lat, lon);

                    return JSONDataParser.parseWeather(jsonData, hotelId, h.getName());
                }
            }
            return JSONDataParser.invalid();
        } finally {
            lock.unlockRead();
        }
    }


}
