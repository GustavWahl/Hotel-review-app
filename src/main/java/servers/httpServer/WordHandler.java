package servers.httpServer;

import hotelapp.ThreadSafeDataStorage;

import java.io.PrintWriter;

/**
 * Handler class for index endpoint
 */
public class WordHandler implements HttpHandler {
    private ThreadSafeDataStorage data;

    public WordHandler(ThreadSafeDataStorage data){
        this.data = data;
    }

    /**
     * Method that processes index endpoint requests
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        String word = request.getParamValue("word");
        String numString = request.getParamValue("num");
        int num = 0;

        if (numString != null){
            num = Integer.parseInt(numString);
        }

        String OUTPUT = data.getIndexJson(word, num);

        writer.println(request.requestOk() + OUTPUT);
    }


    @Override
    public void setAttribute(Object data) {

    }
}
