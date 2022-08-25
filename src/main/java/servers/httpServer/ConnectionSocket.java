package servers.httpServer;

import hotelapp.ThreadSafeDataStorage;
import servers.FetchWeatherData;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Map;

/**
 * Connection socket that handles client
 */
public class ConnectionSocket implements Runnable{
    private final Socket connectionSocket;
    private final ThreadSafeDataStorage data; //TODO more general, maybe a map of resource
    private final Map<String, String> handlers; //TODO use get method

    public ConnectionSocket(Socket socket, ThreadSafeDataStorage data, Map<String, String> handlers){
        this.connectionSocket = socket;
        this.data = data;
        this.handlers = handlers;
    }


    /**
     * Run method for the connection socket
     */
    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(connectionSocket.getOutputStream()), "UTF-8"));

        ) {


            String input;
            input = bufferedReader.readLine();


            HttpRequest httpRequest = new HttpRequest(input);


            if (!httpRequest.isGetRequest()){
               out.println(httpRequest.request405());
            }
            else if (!httpRequest.isValidEndpoint()){
                out.println(httpRequest.request404()); //TODO should be verb
            }
            else {


                HttpHandler httpHandler =  (HttpHandler) Class.forName(handlers.get(httpRequest.getEndpoint())).getConstructor(ThreadSafeDataStorage.class).newInstance(data);
                //TODO again make more general
                httpHandler.processRequest(httpRequest, out);


            }

            out.flush();

        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            System.out.println(e);
        } finally {
            try {
                connectionSocket.close();
            }
            catch (IOException e){
                System.out.println("Error closing connection"); //TODO maybe make it less ugly
            }
        }
    }
}
