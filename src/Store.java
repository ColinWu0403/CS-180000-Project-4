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
        items = new ArrayList<>();
        File f = new File("FMItems.csv");
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = bfr.readLine();
            // Reading lines in file while item info is kept
            // If it belongs to this store, use it information to create an item object
            while (line != null) {
                if (line.substring(0, line.indexOf(",")).equals(storeName)) {
                    String[] splitLine = line.split(",");
                    items.add(new Item(splitLine[0], splitLine[1], splitLine[2],
                            Integer.parseInt(splitLine[3]), Double.parseDouble(splitLine[4])));
                }
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public String getOwner() { return owner; }
    public String getStoreName() { return storeName; }
    // Method to sellers to create new items in their stores
    public void addItem(String itemName, String description, int quantity, double price) {
        items.add(new Item(storeName, itemName, description, quantity, price));
        // Also print item into csv file
        File f = new File("FMItems.csv");
        try {
            FileOutputStream fos = new FileOutputStream(f, true);
            PrintWriter pw = new PrintWriter(fos);
            pw.printf("%s,%s,%s,%d,%f", storeName, itemName, description, quantity, price);
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
    // Method to view specifics of a single item within a store
    public void viewItem(int itemNumber) {
        items.get(itemNumber - 1).printItemInfo();
    }
}
