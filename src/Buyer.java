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
            ArrayList<String> temp = new ArrayList<>();
            temp.add("x");
            this.cart = temp;
        } else {
            this.cart = showItemsInCart(email);
        }
    }

    public void purchaseItem(String itemToPurchase) { // Adds item to purchaseHistoryCSV file
        purchaseHistory.add(itemToPurchase);
    }

//    public static void main(String[] args) { // For Testing
////        Buyer buyer = new Buyer("Brad", "brad@gmail.com", "123",
////                showPurchaseHistory("brad@gmail.com"), showItemsInCart("brad@gmail.com"));
////        ArrayList<String> storesFromProductsSold = buyer.storesFromProductsSold();
//
////        for (int i = 0; i < storesFromProductsSold.size(); i++) {
////            System.out.println(storesFromProductsSold.get(i));
////        }
//
////        storesFromProductsSold = buyer.sortStoresProductsSold();
////        for (int i = 0; i < storesFromProductsSold.size(); i++) {
////            System.out.println(storesFromProductsSold.get(i));
////        }
//
////        ArrayList<String> storesFromBuyerProducts = buyer.storesFromBuyerProducts("jamesz@outlook.com");
////        for (int i = 0; i < storesFromBuyerProducts.size(); i++) {
////            System.out.println(storesFromBuyerProducts.get(i));
////        }
//
////        ArrayList<String> sortedStoresFromBuyerProducts = buyer.sortStoresFromBuyerProducts("jamesz@outlook.com");
////        for (int i = 0; i < sortedStoresFromBuyerProducts.size(); i++) {
////            System.out.println(sortedStoresFromBuyerProducts.get(i));
////        }
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
                    if (purchaseHistoryStr.equals("x")) {
                        return new ArrayList<>();
                    }
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
            String line;
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
//    public ArrayList<String> sortPurchaseHistory() {
//        ArrayList<String> sortedHistory = showPurchaseHistory();
//
//        Collections.sort(sortedHistory);
//
//        return sortedHistory;
//    }


    // returns Arraylist of stores by the products purchased by that particular customer.
    public ArrayList<String> storesFromBuyerProducts(String buyerEmail) {
        try {
            ArrayList<String> stores = parseStore(); // parses store and get ArrayList

            ArrayList<String> storesProductsList = new ArrayList<>();

            for (int i = 0; i < stores.size(); i++) {
                String[] storeSplit = stores.get(i).split(",");
                String storeName = storeSplit[0];
                String productsSold = storeSplit[2];
                storesProductsList.add(storeName + "," + productsSold);
            }

            ArrayList<String> productsPurchased = new ArrayList<>();

            for (int i = 0; i < storesProductsList.size(); i++) {
                String[] listSplit = storesProductsList.get(i).split(",");
                // add number of products sold to Integer list
                String[] productsSplit = listSplit[1].split("~");
                String storeName = listSplit[0];

                for (int j = 0; j < productsSplit.length; j++) {
                    if (productsSplit[j].contains(buyerEmail)) {
                        String[] formatProductSplit = productsSplit[j].split("!");
                        String formatProduct = formatProductSplit[2] + ": Quantity-> " +
                                formatProductSplit[3] + " Price-> $" + formatProductSplit[4];
                        productsPurchased.add(storeName + "," + formatProduct);
                    }
                }
            }

            return productsPurchased;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public ArrayList<String> sortStoresFromBuyerProducts(String email) {
        try {
            ArrayList<String> unsortedList = storesFromBuyerProducts(email);
            ArrayList<Double> prices = new ArrayList<>();
            ArrayList<String> sortedList = new ArrayList<>();

            for (int i = 0; i < unsortedList.size(); i++) {
                String[] listSplit = unsortedList.get(i).split("\\$");
                prices.add(Double.parseDouble(listSplit[1]));
            }

            prices.sort(Collections.reverseOrder());

            for (int i = 0; i < prices.size(); i++) {
                for (int j = 0; j < unsortedList.size(); j++) {
                    if (prices.get(i) == Double.parseDouble(unsortedList.get(j).split("\\$")[1])) {
                        sortedList.add(unsortedList.get(j));
                        unsortedList.remove(j);
                        break;
                    }
                }
            }
            return sortedList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
        ArrayList<String> unsortedList = storesFromProductsSold();
        ArrayList<Integer> productAmt = new ArrayList<>();
        ArrayList<String> sortedList = new ArrayList<>();

        for (int i = 0; i < unsortedList.size(); i++) {
            String[] listSplit = unsortedList.get(i).split(",");
            productAmt.add(Integer.parseInt(listSplit[1]));
        }

        // Sort from most to least
        productAmt.sort(Collections.reverseOrder());

        for (int i = 0; i < productAmt.size(); i++) {
            for (int j = 0; j < unsortedList.size(); j++) {
                if (productAmt.get(i) == Integer.parseInt(unsortedList.get(j).split(",")[1])) {
                    sortedList.add(unsortedList.get(j));
                    unsortedList.remove(j);
                    break;
                }
            }
        }
        return sortedList;
    }

    public void addItem(String itemToAdd, String itemName, int quantityToPurchase) { // add item to shopping cart
        try {
            BufferedReader cartReader = new BufferedReader(new FileReader("FMCredentials.csv"));

            ArrayList<String> FMCredentials = new ArrayList<>();

            // Add existing items to ArrayList;
            String line = "";
            while ((line = cartReader.readLine()) != null) {
                FMCredentials.add(line);
            }
            cartReader.close();

            PrintWriter pwOne = new PrintWriter(new FileWriter("FMCredentials.csv"));

            for (int i = 0; i < FMCredentials.size(); i++) {
                if (FMCredentials.get(i).contains(email)) {
                    String changeLine = FMCredentials.get(i);
                    String[] splitLine = changeLine.split(",");
                    String shoppingCart = changeLine.split(",")[5];
                    if (shoppingCart.equals("x")) {
                        shoppingCart = itemToAdd;
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(itemToAdd);
                        setCart(temp);
                    } else {
                        shoppingCart = shoppingCart + "~" + itemToAdd;
                        cart.add(itemToAdd);
                    }
                    pwOne.printf("%s,%s,%s,%s,%s,%s\n", splitLine[0], splitLine[1], splitLine[2], splitLine[3], splitLine[4], shoppingCart);
                } else {
                    pwOne.println(FMCredentials.get(i));
                }
            }
            pwOne.close();

            BufferedReader itemReader = new BufferedReader(new FileReader("FMItems.csv"));
            ArrayList<String> FMItems = new ArrayList<>();

            // Add existing items to ArrayList;
            String itemLine = "";
            while ((itemLine = itemReader.readLine()) != null) {
                FMItems.add(itemLine);
            }
            itemReader.close();

            PrintWriter pwTwo = new PrintWriter(new FileWriter("FMItems.csv"));

            for (int i = 0; i < FMItems.size(); i++) {
                if (FMItems.get(i).contains(itemName)) {
                    String changeLine = FMItems.get(i);
                    String[] splitLine = changeLine.split(",");
                    int quantity = Integer.parseInt(changeLine.split(",")[3]);
                    quantity = quantity - quantityToPurchase;
                    pwTwo.printf("%s,%s,%s,%d,%s\n", splitLine[0], splitLine[1], splitLine[2], quantity, splitLine[4]);
                } else {
                    pwTwo.println(FMItems.get(i));
                }
            }
            pwTwo.close();


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

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

    public void setCart(ArrayList<String> input) {
        this.cart = input;
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
            double totalPrice = Double.parseDouble(splitList[3]) * Double.parseDouble(splitList[4]);
            cartString = cartString.concat(String.format("(%d) %s from %s; Quantity: %s; Total Price: %.2f\n", (i + 1), splitList[1], splitList[0], splitList[3], totalPrice));
        }
        return cartString;
    }

    /**
     * User checks out Items.
     */
    public void checkout() { //Removes items from cart, puts them in purchase history, adds purchases to respective Store History
        try {
            String currentCartCSVInfo = "";
            String currentHistoryCSVInfo = "";
            BufferedReader cartReader = new BufferedReader(new FileReader("FMCredentials.csv"));


            // Add existing items to ArrayList;
            String line = "";
            while ((line = cartReader.readLine()) != null) {
                if (line.split(",")[0].equals(email)) {
                    currentCartCSVInfo = line.split(",")[5];
                    currentHistoryCSVInfo = line.split(",")[4];
                }
            }
            cartReader.close();

            int cartSize = cart.size();
            purchaseHistory.addAll(cart);
            System.out.println(purchaseHistory);
            ArrayList<String> storedCSVData = csvTemporaryStorage();
            PrintWriter pw = new PrintWriter(new FileWriter("FMCredentials.csv", false));

            if (purchaseHistory.size() == cartSize) {
                pw.println(getEmail() + "," + getName() + "," + getPassword() + ",buyer," + currentCartCSVInfo + ",x");
            } else {
                pw.println(getEmail() + "," + getName() + "," + getPassword() + ",buyer," + currentHistoryCSVInfo + "~" + currentCartCSVInfo + ",x");
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
