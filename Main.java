import java.io.*;
import java.util.*;

public class Main {

    //Main method to retrieve and read CSV file and save items into 2d ArrayList, then run menu method
    public static void main(String[] args) {

        try {
            Scanner lineReader = new Scanner(new File("C:\\Users\\McKenna\\OneDrive - UW-Eau Claire\\Desktop\\Fall 2023\\CS 145\\Competencies\\Large Program\\animals.csv"));
            List<String[]> lines = new ArrayList<String[]>();

            // Iterate through the CSV file line by line, save each line as array into ArrayList 'lines'
            while (lineReader.hasNextLine()) {
                String line = lineReader.nextLine();
                String[] words = line.split(",");
                lines.add(words);

                for (String word : words) {
                    System.out.print(word + " ");
                }
                System.out.println();
            }

            printMenu(lines);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        //No need to include lineReader.close() -- try-catch closes it automatically
    }

    //Method to print menu and take user input when they select a choice
    public static void printMenu(List<String[]> lines) {
        Scanner menu = new Scanner(System.in);
        System.out.println("Please select a menu option: ");
        System.out.println("1: Predator's prey");
        System.out.println("2: list apex predators");
        System.out.println("3: list all producers");
        System.out.println("4: list most flexible consumers");
        System.out.println("5: Tastiest treat");
        System.out.println("6: identify herbivores, omnivores, and carnivores");
        System.out.println("7: Close program");

        int userChoice;

        //Check if user input is NOT an integer. Prints error if not, otherwise stores input into userChoice
        if (!menu.hasNextInt()) {
            System.out.println("Invalid input, please try again.");
            menu.nextLine();
            printMenu(lines);
        } else {
            userChoice = menu.nextInt();

            if (userChoice < 1 || userChoice > 7) {
                System.out.println("Number out of range, please try again.");
                printMenu(lines);
            } else {

                //if input meets criteria, run switch statement to determine method to run based on user's input
                switch (userChoice) {
                    case 1:
                        whatPredEats(lines);
                        break;
                    case 2:
                        listApexPreds(lines);
                        break;
                    case 3:
                        listProducers(lines);
                        break;
                    case 4:
                        listFlexConsumers(lines);
                        break;
                    case 5:
                        listTastiestTreat(lines);
                        break;
                    case 6:
                        listHerbsOmsCarns(lines);
                        break;
                    case 7:
                        menu.close();
                        break;
                }
            }
        }
    }

    //Method to print list of what each predator eats
    public static void whatPredEats(List<String[]> lines) {

        //Iterate through each line in "lines" ArrayList
        for (String[] words : lines) {
            System.out.print(words[0] + " eats: ");
            for (int i = 1; i < words.length; i++) {
                System.out.print(words[i]);
                if (i < words.length - 1) {
                    System.out.print(", ");
                } else {
                    //add new line
                    System.out.println();
                }
            }
        }

        printMenu(lines);
    }

    //Method to read 2d array in columns, print items that appear in first column and ONLY first column
    public static void listApexPreds(List<String[]> lines) {

        //Calculate max # of columns in every row of "lines" list
        int maxColumns = lines.stream().mapToInt(animals -> animals.length).max().orElse(0);

        //HashSet instead of arraylist to avoid duplicates
        //Store items in the first column
        Set<String> apexPreds = new LinkedHashSet<>();
        Set<String> duplicates = new HashSet<>();


        for (String[] animals : lines) {
            if (animals.length > 0) {
                String apexPred = animals[0].toLowerCase();

                //Check if this predator appears in other columns
                for (int i = 1; i < animals.length; i++) {
                    String currentAnimal = animals[i].toLowerCase();
                    if (!duplicates.contains(currentAnimal)) {
                        duplicates.add(currentAnimal);
                    } else {
                        apexPreds.remove(apexPred);
                    }
                }

                // If pred is NOT in other columns (specifically, not in duplicates set), add to set.
                if (!duplicates.contains(apexPred)) {
                    apexPreds.add(apexPred);
                }
            }
        }

        //Print values of apexPreds list, separate by commas
        System.out.print("Apex predators: ");
        int i = 0;
        for (String apexPred : apexPreds) {
            System.out.print(apexPred);
            if (i < apexPreds.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println(); //add new line
            }
            i++;
        }
        printMenu(lines);
    }

    //Method to print last value of each line
    public static void listProducers(List<String[]> lines) {

        //HashSet to store last value of each line
        Set<String> producers = new HashSet<>();

        //Add the last value of each line to "producers"
        for (String[] tokens : lines) {
            if (tokens.length > 0) {
                producers.add(tokens[tokens.length - 1]);
            }
        }

        //Print each producer followed by a comma, except for the last one
        System.out.print("Producers: ");
        int i = 0;
        for (String producer : producers) {
            System.out.print(producer);
            if (i < producers.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.println(); //add new line
            }
            i++;
        }

        printMenu(lines);
    }

    //Method to calculate number of items "eaten" by the first item of each array (i.e., print the first item of longest row)
    public static void listFlexConsumers(List<String[]> lines) {

        //Use a HashMap (not HashSet) to store the count of each item
        Map<String, Integer> flexConsumer = new HashMap<>();
        Map<String, List<String>> prey = new HashMap<>();

        for (String[] animals : lines) {
            if (animals != null && animals.length > 1) {
                String consumer = animals[0].trim();
                //Retrieves current count of token, defaults to 0 if token not present in map. Add 1 to count
                flexConsumer.put(consumer, flexConsumer.getOrDefault(consumer, 0) + animals.length - 1);

                // Store prey in "prey" map by creating new array with elements of original
                // Store consumer (key) and associated prey (values) in "prey" HashMap
                List<String> rowItems = Arrays.asList(Arrays.copyOfRange(animals, 1, animals.length));
                prey.put(consumer, rowItems);
            }
        }

        String mostFlexConsumer = null;
        int maxCount = 0;

        // Retrieves values of prey counts for each consumer and returns the consumer(s) with highest count
        for (Map.Entry<String, Integer> entry : flexConsumer.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFlexConsumer = entry.getKey();
            }
        }

        System.out.println("Most flexible consumer: " + mostFlexConsumer);

        printMenu(lines);
    }

    // Method to determine which item appears most often in rows following first item in row.
    public static void listTastiestTreat(List<String[]> lines) {

        //To store the count of each item as key-value pairs
        Map<String, Integer> tastyTreat = new HashMap<>();

        for (String[] tokens : lines) {
            for (int i = 1; i < tokens.length; i++) {
                String token = tokens[i].trim();
                //Retrieves current count of token, defaults to 0 if token not present in map. Add 1 to count
                tastyTreat.put(token, tastyTreat.getOrDefault(token, 0) + 1);
            }
        }

        String tastiestTreat = null;
        int maxCount = 0;

        //Retrieves values for each item and returns the one with the highest value
        for (Map.Entry<String, Integer> entry : tastyTreat.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                tastiestTreat = entry.getKey();
            }
        }

        System.out.println("Tastiest treat: " + tastiestTreat);

        printMenu(lines);
    }

    //Method to sort items into categories based on whether they consume the final item in each row and/or other items in the row
    public static void listHerbsOmsCarns(List<String[]> lines) {

        //Create set of producers consisting of last item of each row
        Set<String> producers = new HashSet<>();
        for (String[] tokens : lines) {
            if (tokens.length > 0) {
                producers.add(tokens[tokens.length - 1]);
            }
        }

        Set<String> herbivores = new HashSet<>();
        Set<String> omnivores = new HashSet<>();
        Set<String> carnivores = new HashSet<>();

        //Loops to determine what each item "eats"
        for (String[] tokens : lines) {
            boolean eatsProducers = false;
            boolean eatsAnimals = false;

            for (int i = 1; i < tokens.length; i++) {
                String food = tokens[i].trim();

                //Checks if the row following an animal (i.e., its food) contains a producer and/or other animals
                if (producers.contains(food)) {
                    eatsProducers = true;
                } else {
                    eatsAnimals = true;
                }
            }

            //Determine what each animal eats and add to appropriate set
            if (eatsProducers && !eatsAnimals) {
                herbivores.add(tokens[0]);
            } else if (eatsProducers && eatsAnimals) {
                omnivores.add(tokens[0]);
            } else if (!eatsProducers && eatsAnimals) {
                carnivores.add(tokens[0]);
            }
        }

        //Print values of each set
        if (!herbivores.isEmpty()) {
            System.out.println("Herbivores: " + toString(herbivores));
        } else {
            System.out.println("There are no herbivores.");
        }
        if (!omnivores.isEmpty()) {
            System.out.println("Omnivores: " + toString(omnivores));
        } else {
            System.out.println("There are no omnivores.");
        }
        if (!carnivores.isEmpty()) {
            System.out.println("Carnivores: " + toString(carnivores));
        } else {
            System.out.println("There are no carnivores.");
        }

        printMenu(lines);
    }

    //Method to convert values of listHerbsOmsCarns maps to a string to print out as animals rather than a list of map values
    private static String toString(Set<String> set) {
        StringBuilder result = new StringBuilder();
        for (String element : set) {
            result.append(element).append(", ");
        }
        if (!set.isEmpty()) {
            result.delete(result.length() - 2, result.length());
        }
        return result.toString();
    }
}
