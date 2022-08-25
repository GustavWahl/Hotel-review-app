package servers;

import java.io.*;
import java.net.Socket;

/**
 * Class that fetches weather data for custom endpoint
 */
public class FetchWeatherData {

    /**
     * Method that returns and fetches json object from weather data api
     * @param lat
     * @param lon
     * @return
     */
    public static String fetchData(String lat, String lon){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("http://api.openweathermap.org/data/2.5/weather?lat=");
        stringBuilder.append(lat);
        stringBuilder.append("&lon=");
        stringBuilder.append(lon);
        stringBuilder.append("&appid=b939e02c23b91fe014109241f609b28e");

        String url = stringBuilder.toString();

        try { //TODO try with resources or close in finally block
            Socket socket = new Socket("api.openweathermap.org", 80);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()), "UTF-8"));

            String request = "GET https://www.expedia.com/San-Francisco-Hotels-Courtyard-by-Marriott-San-Francisco-Union-Square.h360.Hotel-Information  HTTP/1.1" + System.lineSeparator()
                    + "Host: " + "api.openweathermap.org" + System.lineSeparator()
                    + "Connection: close" + System.lineSeparator() +
                    System.lineSeparator();



            out.write(request);
            out.flush();

            String line = bufferedReader.readLine();
            String json = "";
            while (line != null){
                json += line;
                line = bufferedReader.readLine();
            }

            out.close();
            bufferedReader.close();
            socket.close();

            return json;
        } catch (IOException e){
            System.out.println(e);
        }

        return null;
    }
}
