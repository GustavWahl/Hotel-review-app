package hotelapp;

import hotelapp.DataClasses.Hotel;
import hotelapp.DataClasses.Review;

import java.util.*;

/** Class that handles the command input, after all the input data is loaded in and parsed
 * This is the only class that executes methods based on user input.
 * Class takes input commands and trigger the right methods
 * **/
public class CommandHandler {

    private ThreadSafeDataStorage dataStorage;
    private boolean isPrinting = false;
    private String output;
    /** Constructor
     * @param hotelPath path to the hotelFile
     * @param reviewDirectoryPath path to review directory
     * **/
    public CommandHandler(String hotelPath, String reviewDirectoryPath, int numThreads, String output){
        this.dataStorage = new ThreadSafeDataStorage(hotelPath,reviewDirectoryPath, numThreads);

        if (output != null){
            this.isPrinting = true;
            this.output = output;
        }



    }

    /**
     * Method that triggers Search methods based on commands
     * **/
    public void start(){

        if (!isPrinting){
            Scanner scanner = new Scanner(System.in);
            System.out.println("execute command: ");

            while (scanner.hasNext()) {

                String tmp = scanner.nextLine();
                String[] command = tmp.split(" ");

                if (command.length > 1) {

                    switch (command[0]) {
                        case "find":
                            findHotel(command[1]);
                            break;
                        case "findReviews":
                            findReviews(command[1]);
                            break;
                        case "findWord":
                            findWord(command[1]);
                            break;
                        case "q":
                            System.out.println("User quit");
                            System.exit(0);
                        default:
                            System.out.println("Sorry did not understand that command");
                    }
                } else {
                    System.out.println("need parameter for command");
                }
            }
        } else {

            dataStorage.printToFile(output);
        }
    }

    /** Method that prints hotel data based on hotelId
     * @param hotelId
     * **/
    private void findHotel(String hotelId){

        if (!dataStorage.findHotel(hotelId)) System.out.println("Sorry could not find hotel with that ID");
    }

    /** Method that prints all reviews connected to the hotelId
     * @param hotelId
     * **/
    private void findReviews(String hotelId){

        if (!dataStorage.findReviews(hotelId)) System.out.println("Sorry could not find reviews with that ID");

    }

    /** Method that prints all reviews that includes the word
     * @param word
     * **/
    private void findWord(String word){
        if (!dataStorage.findWord(word)) System.out.println("Sorry could not find reviews with that word");
    }
}
