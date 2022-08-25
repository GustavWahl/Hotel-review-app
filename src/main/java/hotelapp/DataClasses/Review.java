package hotelapp.DataClasses;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Review implements Comparable<Review>{
    private final String hotelId;
    private final String reviewId;
    private final int averageRating;
    private final String title;
    private final String reviewText;
    private final String userNickname;
    private final LocalDate date;

    /** Constructor
     * @param hotelId Hotel id of the review
     * @param reviewId review id
     * @param averageRating the overall / average rating of stay
     * @param title string
     * @param reviewText string
     * @param userNickname string
     * @param date String that gets parsed to Localdate in ISO time
     * **/
    public Review(String hotelId, String reviewId, int averageRating, String title, String reviewText, String userNickname, String date) {
        this.hotelId = hotelId;
        this.reviewId = reviewId;
        this.averageRating = averageRating;
        this.title = title;
        this.reviewText = reviewText;
        if (userNickname.equals("") || userNickname == null){
            this.userNickname = "Anonymous";
        }else {
            this.userNickname = userNickname;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        this.date = LocalDate.parse(date, dateTimeFormatter);
    }

    /** get method for hotel id
     * @return hotelID
     * **/
    public String getHotelId() {
        return hotelId;
    }

    /** get method for review id
     * @return review id
     * **/
    public String getReviewId() {
        return reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    /** get method for date
     * @return localdate date
     * **/
    public LocalDate getDate() {
        return date;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public String getTitle() {
        return title;
    }

    public String getUserNickname() {
        return userNickname;
    }

    /** toString method that returns formatted text based on class data
     * @return String
     * **/
    @Override
    public String toString() {

        return "--------------------" + System.lineSeparator() +
                "Review by " + userNickname + " on " + date + System.lineSeparator() +
                "Rating: " + averageRating + System.lineSeparator() +
                "ReviewId: " + reviewId + System.lineSeparator() +
                title + System.lineSeparator() +
                reviewText + System.lineSeparator();
    }

    /** CompareTo method, compares dates of object or hotel id
     * @param other The other object that its compared against
     * @return returns an int either -1, 0, 1, that is used for sorting
     * **/
    @Override
    public int compareTo(Review other) { //

        int result = this.date.compareTo(other.date);
        if (result == 0) {
            return this.reviewId.compareTo(other.reviewId);
        }
        return -(result);
    }
}
