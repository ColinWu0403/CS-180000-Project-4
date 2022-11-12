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

    //only deletes item from FMItems.csv: need more?
    public void deleteItem() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            // First remove item from FMItems.csv file
            BufferedReader bfrOne = new BufferedReader(new FileReader("FMItems.csv"));
            String line = "";
            while ((line = bfrOne.readLine()) != null) {
                String[] lineSplit = line.split(",");
                if (!name.equals(lineSplit[1])) {
                    lines.add(line);
                }
            }
            bfrOne.close();
            PrintWriter pwOne = new PrintWriter(new FileOutputStream("FMItems.csv", false));
            for (String s : lines) {
                pwOne.println(s);
            }
            pwOne.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // Standard getters and a setter for quantity
    public String getStore() { return store; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public boolean changeField(String field, String newValue) { // Changes value of field, returns true if successful
        // Write quantity change to csv file
        File f = new File("FMItems.csv");
        ArrayList<String> lines = new ArrayList<>();
        // Returns false if the quantity or price would go too low
        if (field.equals("quantity") && Integer.parseInt(newValue) > quantity) { return false; }
        if (field.equals("price") && Double.parseDouble(newValue) >= price) { return false; }
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = bfr.readLine();
            while (line != null) {
                if (line.equals(String.format("%s,%s,%s,%d,%.2f", store, this.name, description, quantity, price))) {
                    if (field.equals("name")) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, newValue, description, quantity, price);
                    } else if (field.equals("description")) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, name, newValue, quantity, price);
                    } else if (field.equals("quantity")) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, name, description, Integer.parseInt(newValue), price);
                    } else if (field.equals("price")) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, name, description, quantity, Double.parseDouble(newValue));
                    }
                }
                lines.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
            for (String s : lines) {
                pw.println(s);
            }
            pw.close();

            switch (field) {
                case "name" -> this.name = newValue;
                case "description" -> this.description = newValue;
                case "quantity" -> this.quantity = Integer.parseInt(newValue);
                case "price" -> this.price = Double.parseDouble(newValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // Method to print product name and price of an item
    public void printItem() {
        System.out.printf("%s selling at %.2f\n", name, price);
    }
    // Method to print more detailed information about an item
    public void printItemInfo() {
        System.out.printf("%s selling at %.2f. %d in stock.\n%s\n", name, price, quantity, description);
    }
}
