package hotelapp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/** Class to handle the parameters when executing the program**/
public class ParameterHandler {

    /**  Static method that retuns a hashmap of the argument input
     * @Param String[] args - an array of strings
     * @Returns HashMap<String,String> - returns an hashmap with key
     * that is paramater and value that is String path to file or directory
     * **/

    public static HashMap<String,String> getParameters(String[] args){
        HashMap<String,String> parameters = new HashMap<>();

        List<String> list = Arrays.asList(args);
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            switch (next) {
                case "-hotels":
                    try {
                        parameters.put("hotels", iterator.next());
                    } catch (Exception e) {
                        System.out.println("please specify file path");
                        System.exit(0);
                    }
                    break;
                case "-reviews":
                    try {
                        parameters.put("reviews", iterator.next());
                    } catch (Exception e) {
                        System.out.println("please specify review directory path");
                        System.exit(0);
                    }
                    break;
                case "-threads":
                    try {
                        parameters.put("threads", iterator.next());
                    } catch (Exception e) {
                        System.out.println("please specify number of threads path");
                        System.exit(0);
                    }
                    break;
                case "-output":
                    try {
                        parameters.put("output", iterator.next());
                    } catch (Exception e) {
                        System.out.println("please specify output path");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("please configure parameters correctly");
                    System.exit(0);
            }

        }

        return parameters;
    }
}
