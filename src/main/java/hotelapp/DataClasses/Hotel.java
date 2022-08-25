package hotelapp.DataClasses;

/** Data class for hotel**/
public class Hotel implements Comparable<Hotel>{

    private final String hotelId;
    private final String name;
    private final String addr;
    private final String city;
    private final String state;
    private final float lat;
    private final float lng;


    /** Constructor
     * @param name name of hotel
     * @param id hotel id
     * @param latitude latitude of the hotel
     * @param longitude longitude of the hotel
     * @param address address of hotel
     * **/
    public Hotel(String name, String id, float longitude, float latitude, String address, String city, String pr) {
        this.name = name;
        this.hotelId = id;
        this.lng = longitude;
        this.lat = latitude;
        this.addr = address;
        this.city = city;
        this.state = pr;
    }

    /** Method that returns hotelId
     * @return hotel id
     * **/
    public String getId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    /** toString method
     * @return String of formatted data from hotel class
     * **/
    @Override
    public String toString() {

        return  System.lineSeparator() + "********************"  + System.lineSeparator()
                + name + ": " + hotelId + System.lineSeparator() +
                 addr + System.lineSeparator() +
                city + ", " + state + System.lineSeparator();
    }

    /** CompareTo method, compares dates of object or hotel id
     * @param other The other object that its compared against
     * @return returns an int either -1, 0, 1, that is used for sorting
     * **/
    @Override
    public int compareTo(Hotel other) { //
        return hotelId.compareTo(other.hotelId);
    }
}
