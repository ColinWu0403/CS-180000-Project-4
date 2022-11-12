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
        if (purchaseHistory == null) { //creating account
            this.purchaseHistory = new ArrayList<>();
        } else {                        //signing in
            this.purchaseHistory = showPurchaseHistory(email);
        }
        if (cart == null) {
            this.cart = new ArrayList<>();
        } else {
            this.cart = showItemsInCart(email);
        }
    }

    public void purchaseItem(String itemToPurchase) { // Adds item to purchaseHistoryCSV file
        purchaseHistory.add(itemToPurchase);
    }

//    public static void main(String[] args) { // For Testing
//        Buyer buyer = new Buyer("Brad", "brad@gmail.com", "123",
//                showPurchaseHistory("brad@gmail.com"), showItemsInCart("brad@gmail.com"));
//        ArrayList<String> storesFromProductsSold = buyer.storesFromProductsSold();
//
//        for (int i = 0; i < storesFromProductsSold.size(); i++) {
//            System.out.println(storesFromProductsSold.get(i));
//        }
//
//        storesFromProductsSold = buyer.sortStoresProductsSold();
//        for (int i = 0; i < storesFromProductsSold.size(); i++) {
//            System.out.println(storesFromProductsSold.get(i));
//        }
//    }

    // Creates a new file of purchase history
    public static void exportPurchaseHistory(String email) {
        try {

            BufferedReader purchasesReader = new BufferedReader(new FileReader("FMCredentials.csv"));

            ArrayList<String> FMCredentials = new ArrayList<>();

            // Read through FMCredentials and append to arraylist
            String line = purchasesReader.readLine();
            while (line != null) {
                FMCredentials.add(line);
                line = purchasesReader.readLine();
            }

            purchasesReader.close();

            // loop through arraylist and find the correct account
            for (int i = 0; i < FMCredentials.size(); i++) {
                // If arraylist index has email
                if (FMCredentials.get(i).contains(email)) {
                    String[] strSplit = FMCredentials.get(i).split(",");
                    String purchaseHistoryStr = strSplit[4];
                    String[] purchaseHistoryLine = purchaseHistoryStr.split("~");

                    // Create export file
                    try {
                        String[] emailSplit = email.split("~");
                        String fileName = emailSplit[0] + "PurchaseHistory.csv";
                        File export = new File(fileName);

                        FileOutputStream fos = new FileOutputStream(export, false);
                        PrintWriter purchaseWriter = new PrintWriter(fos);

                        // Write to file
                        for (int j = 1; j < purchaseHistoryLine.length; j++) {
                            purchaseWriter.println(purchaseHistoryLine[j]);
                        }

                        purchaseWriter.close();
                        System.out.println("File Exported!");
                    } catch (Exception e) {
                        System.out.println("File NOT Exported");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> showPurchaseHistory(String email) { // returns an ArrayList to be printed as the purchase history
        try {
            // Read through CSV file
            BufferedReader purchasesReader = new BufferedReader(new FileReader("FMCredentials.csv"));

            ArrayList<String> FMCredentials = new ArrayList<>();

            // Add existing items to ArrayList;
            String line = purchasesReader.readLine();
            while (line != null) {
                FMCredentials.add(line);
                line = purchasesReader.readLine();
            }

            purchasesReader.close();

            // loop through arraylist and find the correct account
            for (int i = 0; i < FMCredentials.size(); i++) {
                // If arraylist index has email
                if (FMCredentials.get(i).contains(email)) {
                    String[] strSplit = FMCredentials.get(i).split(",");
                    String purchaseHistoryStr = strSplit[4];
                    String[] purchaseHistoryLine = purchaseHistoryStr.split("~");

                    // Return new ArrayList
                    return new ArrayList<>(Arrays.asList(purchaseHistoryLine));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> showItemsInCart(String email) {
        try {
            BufferedReader cartReader = new BufferedReader(new FileReader("FMCredentials.csv"));

            ArrayList<String> FMCredentials = new ArrayList<>();

            // Add existing items to ArrayList;
            String line = "";
            while ((line = cartReader.readLine()) != null) {
                FMCredentials.add(line);
            }
            cartReader.close();

            for (int i = 0; i < FMCredentials.size(); i++) {
                // If arraylist index has email
                if (FMCredentials.get(i).contains(email)) {
                    String[] strSplit = FMCredentials.get(i).split(",");
                    String shoppingCartInfo = strSplit[5];
                    String[] shoppingCartLine = shoppingCartInfo.split("~");

                    return new ArrayList<>(Arrays.asList(shoppingCartLine));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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

                storesProductsList.add(storeName + "," + productsSold);
            }

            for (int i = 0; i < storesProductsList.size(); i++) {
                String[] listSplit = storesProductsList.get(i).split(",");
                // add number of products sold to Integer list
                String[] productsSplit = listSplit[1].split("~");
                String storeName = listSplit[0];
                if (!productsSplit[0].equals("x")) {
                    int productSize = productsSplit.length;
                    storesProductsList.set(i, storeName + "," + productSize);
                } else {
                    storesProductsList.set(i, storeName + ",0");
                }
            }

            return storesProductsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns sorted ArrayList of stores by number of products sold from most to least
    public ArrayList<String> sortStoresProductsSold() {
        ArrayList<String> sortedList = storesFromProductsSold();

        // Sort from most to least
        sortedList.sort(Collections.reverseOrder());

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
            double totalPrice = Double.parseDouble(splitList[2]) * Double.parseDouble(splitList[3]);
            cartString = cartString.concat(String.format("(%d) %s from %s; Quantity: %s; Total Price: %.2f\n", (i + 1), splitList[1], splitList[0], splitList[2], totalPrice));
        }
        return cartString;
    }

    /**
     * User checks out Items.
     */
    public void checkout() { //Removes items from cart, puts them in purchase history, adds purchases to respective Store History
        try {
            purchaseHistory.addAll(cart);
            ArrayList<String> storedCSVData = csvTemporaryStorage();
            PrintWriter pw = new PrintWriter(new FileWriter("FMCredentials.csv", false));

            pw.print(getEmail() + "," + getName() + "," + getPassword() + ",buyer,");
            for (int i = 0; i < purchaseHistory.size(); i++) {
                if (i + 1 == purchaseHistory.size()) {
                    pw.println(purchaseHistory.get(i) + ",");
                } else {
                    pw.print(purchaseHistory.get(i) + "~");
                }
            }
            for (int i = 0; i < storedCSVData.size(); i++) {
                pw.println(storedCSVData.get(i));
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an item from a user's cart.
     *
     * @param userChoice Number that the user selects in the main method.
     */
    public void removeItemFromCart(int userChoice) {
        try {
            cart.remove(userChoice - 1);
            ArrayList<String> storedCSVData = csvTemporaryStorage();
            PrintWriter pw = new PrintWriter(new FileWriter("FMCredentials.csv", false));

            pw.print(getEmail() + "," + getName() + "," + getPassword() + ",buyer,");
            if (purchaseHistory.isEmpty()) {
                pw.print(",");
            }
            for (int i = 0; i < purchaseHistory.size(); i++) {
                if (i + 1 == purchaseHistory.size()) {
                    pw.print(purchaseHistory.get(i) + ",");
                } else {
                    pw.print(purchaseHistory.get(i) + "~");
                }
            }
            for (int i = 0; i < cart.size(); i++) {
                if (i + 1 == cart.size()) {
                    pw.println(cart.get(i));
                } else {
                    pw.print(cart.get(i) + "~");
                }
            }
            for (int i = 0; i < storedCSVData.size(); i++) {
                pw.println(storedCSVData.get(i));
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads all data that isn't related to the user from the Credentials file and stores it
     * for easy rewriting
     *
     * @return stored csv to be used in other buyer Functions
     */
    public ArrayList<String> csvTemporaryStorage() {
        ArrayList<String> csvStorage = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));

            String line = bfr.readLine();
            while (line != null) {
                String[] splitLine = line.split(",");
                if (!splitLine[0].equals(getEmail())) { // saves all data that is not being edited
                    csvStorage.add(line);
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csvStorage;
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

            itemReader.close();
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
            String line;
            while ((line = storeReader.readLine()) != null) {
                parsedList.add(line);
            }

            storeReader.close();
            return parsedList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
