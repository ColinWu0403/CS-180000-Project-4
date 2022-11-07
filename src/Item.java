import java.io.*;
import java.util.ArrayList;

/**
 * Item class, contains all relevant information for an item and print it when necessary
 *
 * @author Dakota Baldwin
 * @version 11/4/2022
 */

public class Item {
    private String store;
    private String name;
    private String description;
    private int quantity;
    private double price;
    // Standard constructor
    public Item(String store, String name, String description, int quantity, double price) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
    // Standard getters and a setter for quantity
    public String getStore() { return store; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public void setQuantity(int quantity) {
        // Write quantity change to csv file
        File f = new File("FMItems.csv");
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                if (line.equals(String.format("%s,%s,%s,%d,%f", store, name, description, this.quantity, price))) {
                    line = String.format("%s,%s,%s,%d,%f", store, name, description, quantity, price);
                }
                lines.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            FileOutputStream fos = new FileOutputStream(f, false);
            PrintWriter pw = new PrintWriter(fos);
            for (String s : lines) {
                pw.println(s);
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.quantity = quantity;
    }
    // Method to print product name and price of an item
    public void printItem() {
        System.out.printf("%s selling at %f\n", name, price);
    }
    // Method to print more detailed information about an item
    public void printItemInfo() {
        System.out.printf("%s selling at %f. %d in stock.\n%s\n", name, price, quantity, description);
    }
}
