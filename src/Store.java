import java.util.ArrayList;
import java.io.*;

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
                String [] splitLine = line.split(",");
                if (splitLine[0].equals(storeName)) {
                    items.add(new Item(splitLine[0],splitLine[1],splitLine[2],Integer.parseInt(splitLine[3]),
                            Double.parseDouble(splitLine[4])));
                }
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getOwner() { return owner; }
    public String getStoreName() { return storeName; }
    public ArrayList<Item> getItems () {
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
    // Method to save sale information for seller
    public void saveSale(String buyer, Item item, int amountSold) {
        try {
            // First print sale to file saving sales for this store
            PrintWriter pwOne = new PrintWriter(new FileOutputStream(storeName + "Sales.csv", true));
            pwOne.printf("%s,%s,%d,%f", buyer, item.getName(), amountSold, amountSold * item.getPrice());
            pwOne.close();
            // Read file holding statistics and update buyer and item numbers if already present
            BufferedReader bfr = new BufferedReader(new FileReader(storeName + "Stats.csv"));
            ArrayList<String> lines = new ArrayList<>();
            boolean buyerFound = false;
            boolean itemFound = false;
            String line = bfr.readLine();
            while (line != null) {
                String[] splitLine = line.split(",");
                if (buyer.equals(splitLine[0])) {
                    buyerFound = true;
                    splitLine[1] = Integer.toString((Integer.parseInt(splitLine[1]) + amountSold));
                } else if (item.getName().equals(splitLine[0])) {
                    itemFound = true;
                    splitLine[1] = Integer.toString((Integer.parseInt(splitLine[1]) + amountSold));
                }
                lines.add(splitLine[0] + "," + splitLine[1]);
                line = bfr.readLine();
            }
            /*
             File will have buyer statistics above item statistics, and this will make sure
             new buyers are printed at top of file
            */
            if (!buyerFound) {
                lines.add(0, buyer + "," + amountSold);
            }
            if (!itemFound) {
                lines.add(item.getName() + "," + amountSold);
            }
            bfr.close();
            // Print updated statistics back to the file
            PrintWriter pwTwo = new PrintWriter(new FileOutputStream(storeName + "Stats.csv", false));
            for (int i = 0; i < lines.size(); i++) {
                pwTwo.println(lines.get(i));
            }
            pwTwo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Method to print sale history of a store for seller
    public void viewSales() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(storeName + "Sales.csv"));
            String line = bfr.readLine();
            while (line != null) {
                String[] splitLine = line.split(",");
                System.out.printf("%s bought %d of %s for %f total.", splitLine[0], Integer.parseInt(splitLine[2]),
                        splitLine[1], Double.parseDouble(splitLine[3]));
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
