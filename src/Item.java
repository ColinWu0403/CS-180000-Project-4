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

    /**
     * Item constructor
     *
     * @param store       Item store name
     * @param name        Item name
     * @param description Item description
     * @param price       Item price
     * @param quantity    Item quantity
     **/
    public Item(String store, String name, String description, int quantity, double price) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Deletes Item from FMItems.csv
     **/
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


    /**
     * Returns store
     **/
    public String getStore() {
        return store;
    }

    /**
     * Returns name
     **/
    public String getName() {
        return name;
    }

    /**
     * Returns description
     **/
    public String getDescription() {
        return description;
    }

    /**
     * Returns quantity
     **/
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns price
     **/
    public double getPrice() {
        return price;
    }

    /**
     * Changes field in of item FMItems.csv
     *
     * @param field    : Field type to change
     * @param newValue : New value to change it to
     **/
    public void changeField(String field, String newValue) {
        // Write quantity change to csv file
        File f = new File("FMItems.csv");
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                if (field.equals("name")) {
                    if (line.equals(String.format("%s,%s,%s,%d,%.2f", store, this.name, description, quantity, price))) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, newValue, description, quantity, price);
                    }
                    lines.add(line);
                    line = bfr.readLine();
                } else if (field.equals("description")) {
                    if (line.equals(String.format("%s,%s,%s,%d,%.2f", store, name, this.description, quantity, price))) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, name, newValue, quantity, price);
                    }
                    lines.add(line);
                    line = bfr.readLine();
                } else if (field.equals("quantity")) {
                    if (line.equals(String.format("%s,%s,%s,%d,%.2f", store, name, description, this.quantity, price))) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, name, description,
                                Integer.parseInt(newValue), price);
                    }
                    lines.add(line);
                    line = bfr.readLine();
                } else if (field.equals("price")) {
                    if (line.equals(String.format("%s,%s,%s,%d,%.2f", store, name, description, quantity, this.price))) {
                        line = String.format("%s,%s,%s,%d,%.2f", store, name, description, quantity,
                                Double.parseDouble(newValue));
                    }
                    lines.add(line);
                    line = bfr.readLine();
                }

            }
            bfr.close();
            FileOutputStream fos = new FileOutputStream(f, false);
            PrintWriter pw = new PrintWriter(fos);
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
    }

    /**
     * Prints store name, name, price, quantity, and description of item
     **/
    public void printItem() {
        System.out.printf("Store: %s :Product: %s :Price: $%.2f :Quantity: %d :Description: %s\n",
                store, name, price, quantity, description);
    }


    /**
     * Prints shorter information of Item
     **/
    public void printItemInfo() {
        System.out.printf("%s selling at %.2f. %d in stock.\n%s\n", name, price, quantity, description);
    }
}
