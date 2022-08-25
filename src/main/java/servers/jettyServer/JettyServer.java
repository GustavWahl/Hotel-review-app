package servers.jettyServer;

import hotelapp.ThreadSafeDataStorage;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/** This class uses Jetty & servlets to implement server serving hotel and review info */
public class JettyServer {
    private static final int PORT = 8090;
    private final ThreadSafeDataStorage data;

    public JettyServer(ThreadSafeDataStorage data){
        this.data = data;
    }

    /**
     * Function that starts the server
     * @throws Exception throws exception if access failed
     */
    public  void start() throws Exception {
        Server server = new Server(PORT); // jetty server

        ServletHandler handler = new ServletHandler();


        handler.addServletWithMapping(new ServletHolder(new HotelServlet(data)), "/hotelInfo");
        handler.addServletWithMapping(new ServletHolder(new IndexServlet(data)), "/index");
        handler.addServletWithMapping(new ServletHolder(new ReviewsServlet(data)), "/reviews");
        handler.addServletWithMapping(new ServletHolder(new WeatherServlet(data)), "/weather");


        server.setHandler(handler);

        server.start();
        server.join();
    }

}
