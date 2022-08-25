package hotelapp;

import servers.httpServer.*;
import servers.jettyServer.JettyServer;

import java.util.HashMap;

/** The driver class for project 4
 * The main function should take the following command line arguments:
 * -hotels hotelFile -reviews reviewsDirectory -threads numThreads -output filepath
 * (order may be different)
 * and read general information about the hotels from the hotelFile (a JSON file),
 * as read hotel reviews from the json files in reviewsDirectory (can be multithreaded or
 * single-threaded).
 * The data should be loaded into data structures that allow efficient search.
 * If the -output flag is provided, hotel information (about all hotels and reviews)
 * should be output into the given file.
 * Then in the main method, you should start an HttpServer that responds to
 * requests about hotels and reviews.
 * See pdf description of the project for the requirements.
 * You are expected to add other classes and methods from project 3 to this project,
 * and take instructor's / TA's feedback from a code review into account.
 * Please download the input folder from Canvas.
 */
public class HttpServerMain {
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("please specify hotel and review data");
            System.exit(0);
        }

        HashMap<String, String> params = ParameterHandler.getParameters(args);

        String hotelFilePath = params.get("hotels");
        String reviewDirectoryPath = params.get("reviews");
        String threads = params.get("threads");
        int numThreads = 1;
        if (threads != null){
            numThreads = Integer.parseInt(threads);
        }
        String output = params.get("output");

        ThreadSafeDataStorage threadSafeDataStorage = new ThreadSafeDataStorage(hotelFilePath,reviewDirectoryPath, numThreads);

       HttpServer server = new HttpServer(true, numThreads, threadSafeDataStorage);


        System.out.println(threadSafeDataStorage.getTot());
        server.addMapping("/hotelInfo", HotelHandler.class.getName());
        server.addMapping("/reviews", ReviewsHandler.class.getName());
        server.addMapping("/index", WordHandler.class.getName());
        server.addMapping("/weather", WeatherHandler.class.getName());


        server.start();
    }

}
