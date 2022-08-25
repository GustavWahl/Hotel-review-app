package servers.httpServer;

import java.util.HashMap;
import java.util.Map;

/** A class that represents an http request */
public class HttpRequest {

    private boolean isGetRequest;
    private Map<String, String> queryParameter;
    private String endpoint;

    /**
     * Constructor
     * @param request
     */
    public HttpRequest(String request){
        isGetRequest = request.startsWith("GET");
        String[] req = request.split(" ");
        queryParameter = new HashMap<>();

        for (String s : req){
            if (s.startsWith("/")){
                String[] url = s.split("\\?");

                endpoint = url[0];

                if (url.length > 1) {
                    String[] queryParameters = url[1].split("&");

                    for (String param : queryParameters) {
                        String[] keyValPair = param.split("=");
                        String key = keyValPair[0];
                        String value = keyValPair[1];

                        queryParameter.put(key, value);
                    }
                }
            }
        }
    }

    /**
     * Method that returns if request is get method
     * @return
     */
    public boolean isGetRequest() {
        return isGetRequest;
    }

    /**
     * Returns status 405
     * @return
     */
    public String request405(){ //TODO Redundant, make one status method
        return  "HTTP/1.1 405 Method not allowed\r\n" + //TODO use line Separator, won't work on mac
                "Content-Type: application/json\r\n" +
                "Allowed: GET" + "\r\n\r\n";
    }

    /**
     * Returns status 200 OK
     * @return
     */
    public String requestOk(){
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + "\r\n\r\n";
    }

    /**
     * returns endpoint
     * @return
     */
    public String getEndpoint(){
        return endpoint;
    }

    /**
     * Method that returns the value of the parameter
     * @param param
     * @return
     */
    public String getParamValue(String param){
        return queryParameter.get(param);
    }

    /**
     * Method that checks if endpoint is valid
     * @return
     */
    public boolean isValidEndpoint() { //TODO have to change for new endpoint
        switch (endpoint){
            case "/hotelInfo":
            case "/reviews":
            case "/index":
            case "weather":
                return true;

        }
        return false;
    }

    /**
     * Method that returns status code 404 not found
     * @return
     */
    public String request404() {
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + "\r\n\r\n";
    }
}
