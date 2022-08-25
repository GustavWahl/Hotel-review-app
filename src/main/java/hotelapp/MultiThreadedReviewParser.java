package hotelapp;


import hotelapp.DataClasses.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * This class that handles multiple threads that parses json file
 *
 * **/
public class MultiThreadedReviewParser {
    private ExecutorService executorService;
   // private Logger logger = LogManager.getLogger();
    private Phaser phaser;
    private  Map<String,List<Review>> reviewMap;

    /**
     * Constructor, initializes executorservice, map and phaser
     * @param numThreads
     */
    public MultiThreadedReviewParser(int numThreads){
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.phaser = new Phaser();
        this.reviewMap = new HashMap<>();
    }

    /**
     * Worker class that calls JSON parser
     */
    public class ReviewParserWorker implements Runnable{
        private Path path;
        private List<Review> reviewList;


        /**
         * Constructor that intializes the path to the file
         * @param p
         */
        public ReviewParserWorker(Path p) {
            this.path = p;
        }

        /**
         * Run method that gets executed when thread starts
         */
        @Override
        public void run() {
            try {
                reviewList = JSONDataParser.parseReviewData(path);
            } finally {
                combineData(reviewList);
                phaser.arrive();

            }
        }


    }


    /**
     * Method that combines local thread data with shared map
     * @param reviewList
     */
    public synchronized void combineData(List<Review> reviewList){
        if (!reviewList.isEmpty()) {
            reviewMap.put(reviewList.get(0).getHotelId(), reviewList);
        }
    }

    /**
     * Method that starts the first thread and returns the map of all reviews
     * @param p
     * @return
     */
    public Map<String, List<Review>> manageExecutor(String p){
        try {
            getPaths(p);
        }catch (IOException e){
            System.out.println(e);
        }

        int phase = phaser.getPhase();
        phaser.awaitAdvance(phase);
        shutdownExecutor();

        return Collections.unmodifiableMap(reviewMap);
    }

    /**
     * Method to shutdown executor service gracefully
     */
    public void shutdownExecutor(){
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e){
            System.out.println(e);
        }
    }

    /** Recursive method that creates a thread for each Path to review Files
     * @Param Path path - the path to the review directory
     * **/
    public void getPaths(String stringPath) throws IOException {

        Path path = Paths.get(stringPath);

        DirectoryStream<Path> files = Files.newDirectoryStream(path);
        Iterator<Path> fileIterator = files.iterator();

        while (fileIterator.hasNext()) {
            Path p = fileIterator.next();
            if (Files.isDirectory(p)){
                getPaths(p.toString());
            }
            else {
                ReviewParserWorker worker = new ReviewParserWorker(p);
                phaser.register();
                executorService.submit(worker);

            }
        }
    }

}
