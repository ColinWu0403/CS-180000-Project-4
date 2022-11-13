import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;

/**
 * A store class, contains pertinent information about the store
 */

public class Store {
    private String owner;
    private String storeName;
    private ArrayList<Item> items;

    public Store(String owner, String storeName) {
        this.owner = owner;
        this.storeName = storeName;

        try {
            items = new ArrayList<>();
            File f = new File("FMItems.csv");
            // Initialize item objects that this store has created
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = bfr.readLine()) != null) {
                String[] splitLine = line.split(",");
                if (splitLine[0].equals(storeName)) {
                    items.add(new Item(splitLine[0], splitLine[1], splitLine[2], Integer.parseInt(splitLine[3]),
                            Double.parseDouble(splitLine[4])));
                }
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Store store = new Store("seller", "store");
        saveSale("buyer", store.items.get(0), 5);
    }
    public String getOwner() {
        return owner;
    }

    public String getStoreName() {
        return storeName;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    // Method to sellers to create new items in their stores
    public void addItem(String itemName, String description, int quantity, double price) {
        items.add(new Item(storeName, itemName, description, quantity, price));
        // Also print item into csv file
        File f = new File("FMItems.csv");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(f, true));
            pw.printf("%s,%s,%s,%d,%.2f\n", storeName, itemName, description, quantity, price);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to print items in a store so buyers can see there options and sellers can see what items they have listed
    public void printItems() {
        for (int i = 0; i < items.size(); i++) {
            System.out.print((i + 1) + ". ");
            items.get(i).printItem();
        }
    }

    //
    public String printItemNames() {
        if (items.size() == 0) {
            System.out.println("Error: No products in this store");
            return "Error";
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println("(" + (i + 1) + ")" + items.get(i).getName());
            }
            return "Success";
        }
    }

    // Method to view specifics of a single item within a store
    public void viewItem(int itemNumber) {
        items.get(itemNumber - 1).printItemInfo();
    }

    //    public static void main(String[] args) { // testing
////        saveSale("JOhn DOe", new Item("John's Desks", "Cedar desk", "desk", 10, 299.99), 2);
//        ArrayList<String> showStats = showStats();
//        for (int i = 0; i < showStats.size(); i++) {
//            System.out.println(showStats.get(i));
//        }
//    }
    // Method to save sale information for seller
    public static void saveSale(String buyer, Item item, int amountSold) {
        try {
            // Read FMStores to find the correct store to add sale information to
            BufferedReader bfrOne = new BufferedReader(new FileReader("FMStores.csv"));
            String line = bfrOne.readLine();
            ArrayList<String> storeFile = new ArrayList<>();
            while (line != null) {
                String[] splitLine = line.split(",");
                // Detects if this is the store we need
                if (splitLine[1].equals(item.getStore())) {
                    // Creates sale section if it doesn't already exist, else adds new sale to end of sale section
                    if (splitLine.length == 2) {
                        line += String.format(",%s!%s!%d!%f", buyer, item.getName(), amountSold, item.getPrice());
                    } else {
                        line += String.format("~%s!%s!%d!%f", buyer, item.getName(), amountSold, item.getPrice());
                    }
                }
                storeFile.add(line);
                line = bfrOne.readLine();
            }
            bfrOne.close();

            // Prints FMStores file with added sale
            PrintWriter pwOne = new PrintWriter(new FileOutputStream("FMStores.csv", false));
            for (int i = 0; i < storeFile.size(); i++) {
                pwOne.println(storeFile.get(i));
            }
            pwOne.close();
            // Read file holding statistics and update buyer and item numbers if already present
            File fmStats = new File("FMStats.csv");
            BufferedReader bfrTwo = new BufferedReader(new FileReader(fmStats));
            ArrayList<String> statsFile = new ArrayList<>();
            boolean buyerFound = false;
            boolean itemFound = false;
            line = bfrTwo.readLine();
            while (line != null) {
                String[] splitLine = line.split(",");
                if (splitLine[0].equals(item.getStore())) {
                    if (buyer.equals(splitLine[1])) {
                        buyerFound = true;
                        splitLine[2] = Integer.toString((Integer.parseInt(splitLine[2]) + amountSold));
                    } else if (item.getName().equals(splitLine[1])) {
                        itemFound = true;
                        splitLine[2] = Integer.toString((Integer.parseInt(splitLine[2]) + amountSold));
                    }
                }
                statsFile.add(String.format("%s,%s,%s,%s", splitLine[0], splitLine[1], splitLine[2], splitLine[3]));
                line = bfrTwo.readLine();
            }
            /*
             File will have buyer statistics above item statistics, and this will make sure
             new buyers are printed at top of file
            */
            if (!buyerFound) {
                statsFile.add(0, String.format("%s,%s,%s,buyer", item.getStore(), buyer, amountSold));
            }
            if (!itemFound) {
                statsFile.add(String.format("%s,%s,%s,item", item.getStore(), item.getName(), amountSold));
            }
            bfrTwo.close();
            // Print updated statistics back to the file
            PrintWriter pwTwo = new PrintWriter(new FileOutputStream("FMStats.csv", false));
            for (int i = 0; i < statsFile.size(); i++) {
                pwTwo.println(statsFile.get(i));
            }
            pwTwo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to print sale history of a store for seller
    public ArrayList<String> showSales() { // returns an ArrayList to be printed as the stores sale history
        try {
            // Read through CSV file
            BufferedReader storeReader = new BufferedReader(new FileReader("FMStores.csv"));
            ArrayList<String> FMStores = new ArrayList<>();

            // Add existing stores to ArrayList;
            String line = storeReader.readLine();
            while (line != null) {
                FMStores.add(line);
                line = storeReader.readLine();
            }
            storeReader.close();

            // loop through arraylist and find the correct store
            for (int i = 0; i < FMStores.size(); i++) {
                // If arraylist index has correct store name
                if (FMStores.get(i).contains(storeName)) {
                    String[] strSplit = FMStores.get(i).split(",");
                    String saleHistoryStr = strSplit[2];
                    String[] saleHistoryLine = saleHistoryStr.split("~");
                    return new ArrayList<>(Arrays.asList(saleHistoryLine));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> showStats(String storeName, String type) { // returns arraylist to be printed as stores statistics
        // type should be either buyer or item
        ArrayList<String> stats = new ArrayList<>();
        try {
            // Read csv file, if line has correct store name, add to array list
            BufferedReader statsReader = new BufferedReader(new FileReader("FMStats.csv"));
            String line = statsReader.readLine();
            while (line != null) {
                String[] splitLine = line.split(",");
                if (splitLine[0].equals(storeName) && splitLine[3].equals(type)) {
                    stats.add(splitLine[1] + "," + splitLine[2]);
                }
                line = statsReader.readLine();
            }
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static ArrayList<String> showSortedStats(String storeName, String type) {
        ArrayList<String> unsorted = showStats(storeName, type);
        ArrayList<Integer> amounts = new ArrayList<>();
        ArrayList<String> sorted = new ArrayList<>();
        for (int i = 0; i < unsorted.size(); i++) {
            amounts.add(Integer.parseInt(unsorted.get(i).substring(unsorted.get(i).indexOf(",") + 1)));
        }
        amounts.sort(Collections.reverseOrder());
        for (int i = 0; i < amounts.size(); i++) {
            for (int j = 0; j < unsorted.size(); j++) {
                if (amounts.get(i) == Integer.parseInt(unsorted.get(j).split(",")[1])) {
                    sorted.add(unsorted.get(j));
                    unsorted.remove(j);
                    break;
                }
            }
        }
        return sorted;
    }
}
