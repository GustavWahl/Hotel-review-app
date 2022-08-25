package hotelapp;

import com.google.gson.*;
import hotelapp.DataClasses.Hotel;
import hotelapp.DataClasses.Review;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.*;

/** Class that takes Json files and parses them to Object**/
public class JSONDataParser {
    private static Gson gson = new Gson();


    /** Helper Method that reads file and returns JSON object
     * @param path
     * @return JsonObject
     * **/
    private static JsonObject getJSONObject(Path path){
        try  {
            FileReader fileReader = new FileReader(path.toFile());
            return gson.fromJson(fileReader, JsonObject.class);

        } catch (FileNotFoundException e){
            System.out.println("file not found");
            System.exit(0);
        }
        return null;
    }

    /** *Method that parses HotelData and returns list of hotel
     * @param path
     * @return List<Hotel>
     * */
    public static List<Hotel> parseHotelData(Path path){
        JsonObject jsonObject = getJSONObject(path);
        JsonArray hotelArray = jsonObject.get("sr").getAsJsonArray();
        List<Hotel> hotels = new ArrayList<>();

        for (JsonElement jsonElement : hotelArray) {

            JsonObject hotel = jsonElement.getAsJsonObject();
            String name = hotel.get("f").getAsString();
            String id = hotel.get("id").getAsString();
            String address = hotel.get("ad").getAsString();
            String city = hotel.get("ci").getAsString();
            String pr = hotel.get("pr").getAsString();

            JsonObject ll = hotel.get("ll").getAsJsonObject();

            float latitude = ll.get("lat").getAsFloat();
            float longitude = ll.get("lng").getAsFloat();


            Hotel h = new Hotel(name, id, longitude, latitude, address, city, pr);

            hotels.add(h);
        }
        Collections.sort(hotels);

        return Collections.unmodifiableList(hotels);
    }

    /** Method that parses review data and returns list of reviews, method also calls setReviewText
     * @param path
     * @return list<Review>
     * **/
    public static List<Review> parseReviewData(Path path){
        JsonObject jsonObject = getJSONObject(path);
        if (jsonObject == null) return null;

        //Even if get can throw nullpointer exception, The exception will be caught earlier if file is missing
        // If the json Data structure and naming changes then get will throw exception
        JsonArray reviewArray = jsonObject.get("reviewDetails").getAsJsonObject()
                    .get("reviewCollection").getAsJsonObject()
                    .get("review").getAsJsonArray();

        List<Review> reviews = new ArrayList<>();


        for (JsonElement jsonElement : reviewArray) {
            JsonObject review = jsonElement.getAsJsonObject();
            String hotelId = review.get("hotelId").getAsString();
            String reviewId = review.get("reviewId").getAsString();
            String title = review.get("title").getAsString();
            String reviewText = review.get("reviewText").getAsString();
            String userNickname = review.get("userNickname").getAsString();
            int ratingOverall = review.get("ratingOverall").getAsInt();
            String reviewSubmissionTime = review.get("reviewSubmissionTime").getAsString();

            Review r = new Review(hotelId, reviewId, ratingOverall, title, reviewText, userNickname, reviewSubmissionTime);
            reviews.add(r);

        }
        Collections.sort(reviews);


        return Collections.unmodifiableList(reviews);
    }

    /**
     * Method that returns json string from hotel object
     * @param hotel
     * @return
     */
    public static String hotelToJson(Hotel hotel){

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("success", true);
        jsonObject.addProperty("hotelId", hotel.getId());

        jsonObject.addProperty("name", hotel.getName());
        jsonObject.addProperty("addr", hotel.getAddr());
        jsonObject.addProperty("city", hotel.getCity());
        jsonObject.addProperty("state", hotel.getState());
        jsonObject.addProperty("lat", hotel.getLat());
        jsonObject.addProperty("lng", hotel.getLng());



        return jsonObject.toString();
    }


    /**
     * method that returns json object with reviews
     * @param reviewList
     * @param num
     * @param id
     * @return
     */
    public static String reviewToJson(List<Review> reviewList, int num, String id){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.addProperty("hotelId", id);

        JsonArray jsonArray = new JsonArray();

        for (Review review : reviewList) {
            if (num == 0) break;

            JsonObject reviewObject = new JsonObject();

            reviewObject.addProperty("reviewId", review.getReviewId());
            reviewObject.addProperty("title", review.getTitle());
            reviewObject.addProperty("user", review.getUserNickname());
            reviewObject.addProperty("reviewText", review.getReviewText());
            reviewObject.addProperty("date", review.getDate().toString());


            jsonArray.add(reviewObject);
            num--;
        }
        jsonObject.add("reviews", jsonArray);

        return jsonObject.toString();
    }

    /**
     * Method that returns reviews that includes word
     * @param reviewSet
     * @param num
     * @param word
     * @return
     */
    public static String reviewsFromWord(Set<Review> reviewSet, int num, String word) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.addProperty("word", word);

        JsonArray jsonArray = new JsonArray();

        for (Review review : reviewSet) {
            if (num == 0) break;

            JsonObject reviewObject = new JsonObject();

            reviewObject.addProperty("reviewId", review.getReviewId());
            reviewObject.addProperty("title", review.getTitle());
            reviewObject.addProperty("user", review.getUserNickname());
            reviewObject.addProperty("reviewText", review.getReviewText());
            reviewObject.addProperty("date", review.getDate().toString());


            jsonArray.add(reviewObject);
            num--;
        }
        jsonObject.add("reviews", jsonArray);

        return jsonObject.toString();
    }

    /**
     * method that returns json object of invalid hotelId
     * @return
     */
    public static String invalidId(){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("success", false);
        jsonObject.addProperty("hotelId", "invalid");

        return jsonObject.toString();
    }

    /**
     * Method that returns invalid word json object
     * @return
     */
    public static String invalidWord() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("success", false);
        jsonObject.addProperty("word", "invalid");

        return jsonObject.toString();
    }

    /**
     * Method that returns invalid long lat for custom endpoint
     * @return
     */
    public static String invalid() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("success", false);
        jsonObject.addProperty("lat || lon", "invalid");

        return jsonObject.toString();
    }

    /**
     * Method that returns weather object from weather data nad hotel id
     * @param jsonData
     * @param hotelId
     * @param name
     * @return
     */
    public static String parseWeather(String jsonData, String hotelId, String name) {

        JsonObject weather = new JsonParser().parse(jsonData).getAsJsonObject();

        weather.remove("visibility");
        weather.remove("coord");
        weather.remove("base");
        weather.remove("dt");
        weather.remove("sys");
        weather.remove("timezone");
        weather.remove("id");
        weather.remove("cod");
        weather.remove("clouds");


        JsonObject hotel = new JsonObject();
        hotel.addProperty("hotelId", hotelId);
        hotel.addProperty("name", name);
        hotel.add("weatherInformation", weather);


        return hotel.toString();
    }


}
