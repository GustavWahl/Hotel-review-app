package servers.httpServer;

import hotelapp.ThreadSafeDataStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Implements an http server using raw sockets */
public class HttpServer {
    private Map<String, String> handlers; // maps each url path to the appropriate handler
    private boolean alive;
    private ThreadSafeDataStorage data; //TODO make more general... mauybe Object
    private final ExecutorService executorService;


    /**
     * Constructor
     * @param alive
     * @param threads
     * @param data
     */
    public HttpServer(boolean alive, int threads, ThreadSafeDataStorage data){
        this.alive = alive;
        this.data = data;
        this.handlers = new HashMap<>();

        executorService = Executors.newFixedThreadPool(threads);
    }

    /**
     * Method that maps enpoint to handler class
     * @param name
     * @param handlerClass
     */
    public void addMapping(String name, String handlerClass){
        handlers.put(name, handlerClass);
    }

    /**
     * Start method for the server, listens for connections and creates new connection sockets on new threads
     */
    public void start() {
        ServerSocket welcomingSocket = null;
        ConnectionSocket connectionSocket = null;

        try {
            welcomingSocket = new ServerSocket(8082);
            while (alive) {

                connectionSocket = new ConnectionSocket(welcomingSocket.accept(), data, handlers); // METHOD IS blocking and waits for connection
                //TODO breaks encapsulation should return unmodifiable Map, change name

                executorService.submit(connectionSocket);


            }
        } catch (IOException exception){
            System.out.println("IO exception");
        }
        shutdownExecutor();
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



}
