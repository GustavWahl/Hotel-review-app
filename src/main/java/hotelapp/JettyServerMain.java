package hotelapp;

import servers.httpServer.*;
import servers.jettyServer.JettyServer;

import java.util.HashMap;

public class JettyServerMain {
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

        JettyServer jettyServer = new JettyServer(threadSafeDataStorage);

        try {
            jettyServer.start();
        } catch (Exception e){
            System.out.println(e);
        }


    }
}
