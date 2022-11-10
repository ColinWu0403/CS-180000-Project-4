import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Buyers class - contains all methods the buyers may use
 *
 * @author Colin Wu
 * @version 2022-3-11
 */
public class Buyer {
    private String name; // Buyer username
    private final String email; // Buyer email - This is the unique identifier (Cannot be changed)
    private String password; // Account Password
    private ArrayList<String> purchaseHistory;
    private ArrayList<String> cart;


    public Buyer(String name, String email, String password, ArrayList<String> purchaseHistory, ArrayList<String> cart) { // Construct Buyers Object
        this.name = name;
        this.email = email;
        this.password = password;
        if (purchaseHistory == null) {
            this.purchaseHistory = new ArrayList<>();
        } else {
            this.purchaseHistory = purchaseHistory;
        }
        if (cart == null) {
            this.cart = new ArrayList<>();
        } else {
            this.cart = cart;
        }
    }

    public void purchaseItem(String itemToPurchase) { // Adds item to purchaseHistoryCSV file
        purchaseHistory.add(itemToPurchase);
    }

   /* public ArrayList<String> showPurchaseHistory() { // returns an ArrayList to be printed as the purchase history
        try {
            // Read through CSV file
            BufferedReader purchasesReader = new BufferedReader(new FileReader(getPurchaseHistoryName()));

            ArrayList<String> purchaseHistory = new ArrayList<>(); // shopping cart ArrayList

            // Add existing items to ArrayList;
            String line = purchasesReader.readLine();
            while (line != null) {
                purchaseHistory.add(line);
                line = purchasesReader.readLine();
            }

            return purchaseHistory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    // Returns ArrayList of sorted purchase history in alphabetical order
    /*public ArrayList<String> sortPurchaseHistory() {
        ArrayList<String> sortedHistory = showPurchaseHistory();

        Collections.sort(sortedHistory);

        return sortedHistory;
    }*/

    // returns ArrayList of stores by number of products sold
    public ArrayList<String> storesFromProductsSold() {
        try {
            ArrayList<String> stores = parseStore(); // parses store and get ArrayList
            ArrayList<String> storesProductsList = new ArrayList<>();

            for (int i = 0; i < stores.size(); i++) {
                String[] storeSplit = stores.get(i).split(",");
                String storeName = storeSplit[0];
                String productsSold = storeSplit[2];

                /* Formatting:
                /* storeName,productsSold */
                storesProductsList.add(storeName + "," + productsSold);
            }

            return storesProductsList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Returns sorted ArrayList of stores by number of products sold from most to least
    public ArrayList<String> sortStoresProductsSold() {
        ArrayList<String> sortedList = new ArrayList<>();
        ArrayList<String> accessList = storesFromProductsSold();
        ArrayList<Integer> productsSoldList = new ArrayList<>();

        for (int i = 0; i < accessList.size(); i++) {
            String[] listSplit = accessList.get(i).split(",");
            // add number of products sold to Integer list
            productsSoldList.add(Integer.parseInt(listSplit[1]));

        }

        // Sort from most to least
        productsSoldList.sort(Collections.reverseOrder());

        for (int i = 0; i < productsSoldList.size(); i++) {
            for (int j = 0; j < accessList.size(); j++) {
                // Note: currently this can't have two stores with the same amount of products sold
                if (accessList.get(j).contains(String.valueOf(productsSoldList.get(i)))) {
                    sortedList.add(accessList.get(j));
                }
            }
        }

        return sortedList;
    }

    public void addItem(String itemToAdd) { // add item to shopping cart
        cart.add(itemToAdd);
    }

    public void removeShoppingCartItem(int itemID) { // remove item from shopping cart
        // TODO: Code not finalized
        /** NOTE: not sure about how exactly to remove an item from the cart
         * Current file formatting (Ex: "John's Chairs",awesome chair","3","39.99").
         * Items may have the same store or item name, so we would need a unique identifier for each item in the cart
         * Maybe we can change the formatting to: "1","John's Chairs",awesome chair","3","39.99"
         * with an item ID that allows the user to just input an integer to remove an item from the cart?
         * Otherwise, I'll change the code to remove by item name I guess.
         * **/
        /*try {
            // Read through CSV file
            BufferedReader cartReader = new BufferedReader(new FileReader(getShoppingCartName()));

            ArrayList<String> shoppingCart = new ArrayList<>(); // shopping cart ArrayList

            // Add existing items to ArrayList;
            String line = cartReader.readLine();
            while (line != null) {
                shoppingCart.add(line);
                line = cartReader.readLine();
            }

            for (int i = 0; i < shoppingCart.size(); i++) { // If itemID matches, remove item from Arraylist;
                int shoppingCartID = Integer.parseInt(shoppingCart.get(i).substring(0,1)); // remove by ID
                if (itemID == shoppingCartID) {
                    shoppingCart.remove(i);
                }
            }

            // Write updated shopping cart to CSV file
            FileOutputStream fos = new FileOutputStream(getShoppingCartName(), false);
            PrintWriter cartWriter = new PrintWriter(fos);

            for (String s : shoppingCart) { // Update file
                cartWriter.println(s);
                cartWriter.flush();
            }

            cartReader.close();
            cartWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /*public void checkOut() { // Checkout all items from shopping cart / set shopping cart to empty
        try {
            FileOutputStream fos = new FileOutputStream(getShoppingCartName(), false);
            PrintWriter cartWriter = new PrintWriter(fos);

            cartWriter.println(""); // Remove all cart items

            cartWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    public void setName(String Name) {
        this.name = name;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<String> getCart() {
        return this.cart;
    }
    public String printCart() { //@"Bob's Tables"!"solid table"!"1"!"79.99"
        String cartString = "";
        for (int i = 0; i < cart.size(); i++) {
            String[] splitList = cart.get(i).split("!");
            double totalPrice = Integer.parseInt(splitList[2]) * Double.parseDouble(splitList[3]);
            cartString = cartString.concat(String.format("%s from %s; Quantity: %s; Total Price: %.2f\n", splitList[1],splitList[0],splitList[2],totalPrice));
        }
        return cartString;
    }
    public ArrayList<String> getPurchaseHistory() {
        return this.purchaseHistory;
    }
    public void deleteAccount() {
        String line;
        StringBuilder credentialsFile = new StringBuilder();
        try {
            // First remove user from credentials file
            BufferedReader bfrOne = new BufferedReader(new FileReader("FMCredentials.csv"));
            line = bfrOne.readLine();
            while (line != null) {
                // Only saves account to reprint to the file if they don't have the email belonging to this account
                if (!email.equals(line.substring(0, line.indexOf(",")))) credentialsFile.append(line).append("\n");
                line = bfrOne.readLine();
            }
            bfrOne.close();
            PrintWriter pwOne = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pwOne.println(credentialsFile);
            pwOne.close();
        } catch (Exception e) {
            System.out.println("Error deleting user credentials!");
            e.printStackTrace();
        }
    }

    // Reads through FMItems.csv and returns a String ArrayList of items
    public ArrayList<String> parseItem() {
        try {
            // Read through CSV file
            BufferedReader itemReader = new BufferedReader(new FileReader("FMItems.csv"));

            ArrayList<String> parsedList = new ArrayList<>();

            // Add existing items to ArrayList;
            String line = itemReader.readLine();
            while (line != null) {
                parsedList.add(line);
                line = itemReader.readLine();
            }

            return parsedList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Reads through FMStores.csv and returns a String ArrayList of items
    public ArrayList<String> parseStore() {
        try {
            // Read through CSV file
            BufferedReader storeReader = new BufferedReader(new FileReader("FMStores.csv"));

            ArrayList<String> parsedList = new ArrayList<>();

            // Add existing items to ArrayList;
            String line = storeReader.readLine();
            while (line != null) {
                parsedList.add(line);
                line = storeReader.readLine();
            }

            return parsedList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
