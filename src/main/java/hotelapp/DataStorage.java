package hotelapp;

import hotelapp.DataClasses.Hotel;
import hotelapp.DataClasses.Review;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class initialises the data structures and parsing, it also holds all the data
 * **/
public class DataStorage {
    protected Map<String,List<Review>> reviews; //TODO remove protected
    protected List<Hotel> hotels; //Could probably remove this as well... one map for review and hotel...

    protected Map<String, Set<Review>> invertedIndex;

    /** Constructor initialises paths, json parse class and inverted index**/
    public DataStorage(String hotelFilePath, String reviewDirectoryPath) {
        this.invertedIndex = new HashMap<>();

        initialiseHotelData(hotelFilePath);
        if (reviewDirectoryPath != null) {
            initialiseReviewData(reviewDirectoryPath);
        }
    }


    /** Method that initialises hotel data**/
    protected void initialiseHotelData(String hotelFilePath) {
        Path hotelPath = Paths.get(hotelFilePath);
        hotels = JSONDataParser.parseHotelData(hotelPath);
    }

    /** Method that initialises the review data and sorts the list,
     * then put the data in a HashMap
     * method also calls the createInvertedIndex method
     * **/
    protected void initialiseReviewData(String reviewDirectoryPath) {
        MultiThreadedReviewParser multiThreadedReviewParser = new MultiThreadedReviewParser(1);
        reviews = new HashMap<>();


            reviews = multiThreadedReviewParser.manageExecutor(reviewDirectoryPath);


        createInvertedIndex();
    }

    /** Method that creates inverted index data structure**/
    private void createInvertedIndex(){
        Map<String,Integer> frequency = new HashMap<>();
        String[]  wordsToRemove = { "a", "the", "is", "are", "were", "and", ",", ".", ";", "!", "..."};


        reviews.forEach((key, val) -> {
            for (Review r : val){
                String text = r.getReviewText();

                text = text.toLowerCase();

                for (String s : wordsToRemove) {
                    if (text.contains(s)){
                        text = text.replaceAll(s, " ");
                    }
                }

                for (String word : text.split(" ")){
                    if (!invertedIndex.containsKey(word)) {
                        Set<Review> reviews = new HashSet<>();
                        reviews.add(r);
                        invertedIndex.put(word, reviews);
                    } else if (!invertedIndex.get(word).isEmpty()){
                        invertedIndex.get(word).add(r);
                    }

                    if (!frequency.containsKey(word)){
                        frequency.put(word,1);
                    } else {
                        int count = frequency.get(word);
                        count++;
                        frequency.put(word,count);
                    }
                }
            }
        });
        //System.out.println(frequency.get("nice"));
    }



    /** Method that prints hotel data based on hotelId
     * @param hotelId
     * **/
  public boolean findHotel(String hotelId){

        for (Hotel h : hotels){
            if (h.getId().equals(hotelId)){
                System.out.println(h.toString());
                return true;
            }
        }
        return false;
  }

    /** Method that prints all reviews connected to the hotelId
     * @param hotelId
     * **/
    public boolean findReviews(String hotelId) {

        List<Review> rw;
        if ( (rw = reviews.get(hotelId)) != null) {
            for (Review r : rw) {
                System.out.println(r.toString());
            }
            return true;
        }
        return false;
    }

    /** Method that prints all reviews that includes the word
     * @param word
     * **/
    public boolean findWord(String word) {

        if (invertedIndex.containsKey(word)){

            System.out.println(invertedIndex.get(word).toString());

            return true;
        }

         return false;
    }
}
