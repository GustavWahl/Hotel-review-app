package servers.httpServer;

import hotelapp.ThreadSafeDataStorage;

import java.io.PrintWriter;

/**
 * Hotel request handler class
 */
public class HotelHandler implements HttpHandler {
    private ThreadSafeDataStorage data;

    public HotelHandler(ThreadSafeDataStorage data){
        this.data = data;
    }

    /**
     * Method that processes the get request
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        String hotelId = request.getParamValue("hotelId");


        String OUTPUT = data.getHotelJsonObject(hotelId);

        writer.println(request.requestOk() + OUTPUT);
    }

    @Override
    public void setAttribute(Object data) {

    }
}
