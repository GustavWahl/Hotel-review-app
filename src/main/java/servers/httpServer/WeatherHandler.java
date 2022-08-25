package servers.httpServer;

import hotelapp.JSONDataParser;
import hotelapp.ThreadSafeDataStorage;
import servers.FetchWeatherData;

import java.io.PrintWriter;

/**
 * Handler class for weather endpoint
 */
public class WeatherHandler implements HttpHandler {
    private ThreadSafeDataStorage data;

    public WeatherHandler(ThreadSafeDataStorage data){
        this.data = data;
    }

    /**
     * Method that processes weather requests
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        String hotelId = request.getParamValue("hotelId");

        String OUTPUT = data.getWeather(hotelId);


        writer.println(request.requestOk() + OUTPUT);
    }

    @Override
    public void setAttribute(Object data) {

    }
}