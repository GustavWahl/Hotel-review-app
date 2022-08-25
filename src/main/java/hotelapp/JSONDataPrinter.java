package hotelapp;

import customLock.ReentrantReadWriteLock;
import hotelapp.DataClasses.Hotel;
import hotelapp.DataClasses.Review;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONDataPrinter {

    public static void printToFile(List<Hotel> hotelList, Map<String, List<Review>> reviewMap, String output){
            try {
                File file = new File(output);

                file.createNewFile();

                FileWriter fileWriter = new FileWriter(file);

                Iterator<Hotel> iterator = hotelList.iterator();

                while (iterator.hasNext()){
                    Hotel hotel = iterator.next();
                    fileWriter.write(hotel.toString());

                    if (reviewMap != null) {
                        List<Review> reviewList = reviewMap.get(hotel.getId());

                        if (reviewList != null) {
                            Iterator<Review> reviewIterator = reviewList.iterator();

                            while (reviewIterator.hasNext()) {
                                Review review = reviewIterator.next();
                                fileWriter.write(review.toString());
                            }

                        }
                    }
                }

                fileWriter.close();

            } catch (IOException e){
                System.out.println(e);
            }
    }
}
