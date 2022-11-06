import java.io.*;
import java.util.ArrayList;

/**
 * Buyers class - contains all methods the buyers may use
 *
 * @author Colin Wu
 * @version 2022-3-11
 */
public class Buyer implements User {
    private String name; // Buyer username
    private final String email; // Buyer email - This is the unique identifier (Cannot be changed)
    private String password; // Account Password
    private ArrayList<String> purchaseHistory; // Products bought or purchase history
    private final String shoppingCartName; // Name of shopping cart CSV file
    private final String purchaseHistoryName; // Name of shopping cart CSV file

    public Buyer(String name, String email, String password) { // Construct Buyers Object
        this.name = name;
        this.email = email;
        this.password = password;
        this.shoppingCartName = setCartFileName(email);
        this.purchaseHistoryName = setPurchaseHistoryFileName(email);
    }
    public String setCartFileName(String email) { // Sets shopping cart filename
        return email + "Cart.csv";
    }
    public String setPurchaseHistoryFileName(String email) { // Sets shopping cart filename
        return email + "PurchaseHistory.csv";
    }

    public void purchaseItem(String itemToPurchase) { // Adds item to purchaseHistoryCSV file
        try {
            File purchaseHistoryCSV = new File(getPurchaseHistoryName());

            // Append added item to shopping cart
            FileOutputStream fos = new FileOutputStream(purchaseHistoryCSV, true);
            PrintWriter purchasesWriter = new PrintWriter(fos);

            purchasesWriter.println(itemToPurchase);

            purchasesWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> showPurchaseHistory() { // returns an ArrayList to be printed as the purchase history
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
    }

    public void createShoppingCart() { // create shopping cart
        try {
            File shoppingCartCSV = new File(getShoppingCartName()); // Create new shopping cart file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(String itemToAdd) { // add item to shopping cart
        /** NOTE: right now String itemToAdd would be the entire line of the shopping cart to add
         * (Ex: "1,John's Chairs",awesome chair","3","39.99").
         * Later in the main interface we'll have to make a parseItem method to first access the specific search Item object
         * and turn it into a string. Not sure if toString() would work? **/
        try {
            File shoppingCartCSV = new File(getShoppingCartName());

            // Append added item to shopping cart
            FileOutputStream fos = new FileOutputStream(shoppingCartCSV, true);
            PrintWriter cartWriter = new PrintWriter(fos);

            cartWriter.println(itemToAdd);

            cartWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeShoppingCartItem(int itemID) { // remove item from shopping cart
        try {
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
        }
    }

    public void checkOut() { // Checkout all items from shopping cart / set shopping cart to empty
        try {
            FileOutputStream fos = new FileOutputStream(getShoppingCartName(), false);
            PrintWriter cartWriter = new PrintWriter(fos);

            cartWriter.println(""); // Remove all cart items

            cartWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    public String getShoppingCartName() {
        return shoppingCartName;
    }

    public String getPurchaseHistoryName() {
        return purchaseHistoryName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPurchaseHistory(ArrayList<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Override
    public void setName(String Name) {
        this.name = name;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void sendMessage(String message) {
        // Not sure if this is needed
    }

    @Override
    public String checkMessage() {
        return null; // Not sure if this is needed
    }

    @Override
    public void deleteAccount() {
        // Not sure if this is needed
    }
}
