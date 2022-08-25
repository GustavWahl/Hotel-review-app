package servers.httpServer;

import hotelapp.ThreadSafeDataStorage;

import java.io.PrintWriter;

/**
 * Handler class for review requests
 */
public class ReviewsHandler implements HttpHandler {
    private ThreadSafeDataStorage data;

    public ReviewsHandler(ThreadSafeDataStorage data){
        this.data = data;
    }

    /**
     * Method that processes review requests
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        String hotelId = request.getParamValue("hotelId");
        String numString = request.getParamValue("num");
        int num = 0;

        if (numString != null){
            num = Integer.parseInt(numString);
        }



        String OUTPUT = data.getReviewJsonObject(hotelId, num);

        writer.println(request.requestOk() + OUTPUT);
    }

    @Override
    public void setAttribute(Object data) {

    }
}
