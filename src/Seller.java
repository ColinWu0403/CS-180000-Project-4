import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The Seller class and all the variables and methods they may use.
 *
 * @author Colin Wu
 * @version 2022-3-11
 */
public class Seller implements User {
    private String name; // the user's name
    private final String email; // the user's email (cannot be changed)
    private String password; // the user's password
    private ArrayList<Store> stores; // a list of stores the seller owns

    public Seller(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

        try {
            stores = new ArrayList<>();
            File f = new File("FMStores.csv");
            // Initialize store objects that this seller has created
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = bfr.readLine();
            while (line != null) {
                String[] splitLine = line.split(",");
                if (splitLine[1].equals(email)) {
                    stores.add(new Store(splitLine[1], splitLine[0]));
                }
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createItem() {
        return null;
    }

    public Item publishItem(String item, String store) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("FMItems.csv"));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("FMItems.csv")));

        StringBuilder fileContents = new StringBuilder();

        // appends all the contents of the marketplace file to a StringBuilder object
        String line;
        do {
            line = reader.readLine();
            if (line != null) {
                fileContents.append(line).append("\n");
                // if statement not to append the last line of the file which is blank
            }
        } while (line != null);

        // append the new item line to the file contents
        String toAppend = store + ";" + item; // store name in front of item as per formatting guidelines
        fileContents.append(toAppend);

        // write the new file back to the main marketplace text file
        writer.println(fileContents);

        // create a new Item object and return it
        String name = item.split(",")[0];
        String description = item.split(",")[1];
        int quantity = Integer.parseInt(item.split(",")[2]);
        double price = Double.parseDouble(item.split(",")[3]);

        return new Item(store, name, description, quantity, price);
    }

    public void createStore(Store store) {
        stores.add(store);
        try {
            PrintWriter printStore = new PrintWriter(new FileOutputStream("FMStores.csv", true));
            printStore.println(store.getStoreName() + "," + store.getOwner());
            printStore.flush();
            printStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStore(Store currentStore) {
        stores.remove(currentStore);
        String line;
        StringBuilder storesFile = new StringBuilder();
        StringBuilder itemsFile = new StringBuilder();
        try {
            // First, remove store belonging to this owner from stores file
            BufferedReader bfrTwo = new BufferedReader(new FileReader("FMStores.csv"));
            line = bfrTwo.readLine();
            int counter = 0;
            while (line != null) {
                String[] splitLine = line.split(",");
                if (!currentStore.getStoreName().equals(splitLine[0])) {
                    if (counter >= 1) {
                        storesFile.append("\n");
                    }
                    storesFile.append(line);
                    counter++;
                }
                line = bfrTwo.readLine();
            }
            bfrTwo.close();
            PrintWriter pwTwo = new PrintWriter(new FileOutputStream("FMStores.csv", false));
            if (storesFile.length() != 0) {
                pwTwo.println(storesFile);
            }
            pwTwo.close();
        } catch (Exception e) {
            System.out.println("Error deleting current store!");
            e.printStackTrace();
        }
        try {
            // Second, remove all items belonging to this store from items file
            BufferedReader bfrThree = new BufferedReader(new FileReader("FMItems.csv"));
            line = bfrThree.readLine();
            int counter = 0;
            while (line != null) {
                // Only saves items whose store doesn't match any of this owner's stores
                String[] splitLine = line.split(",");
                if (!currentStore.getStoreName().equals(splitLine[0])) {
                    if (counter >= 1) {
                        itemsFile.append("\n");
                    }
                    itemsFile.append(line);
                    counter++;
                }
                line = bfrThree.readLine();
            }
            bfrThree.close();
            PrintWriter pwThree = new PrintWriter(new FileOutputStream("FMItems.csv", false));
            if (itemsFile.length() != 0) {
                pwThree.println(itemsFile);
            }
            pwThree.close();
        } catch (Exception e) {
            System.out.println("Error deleting store items!");
            e.printStackTrace();
        }
    }

    public void exportPublishedItems(String storeName) {
        try {
            BufferedReader itemReader = new BufferedReader(new FileReader("FMItems.csv"));
            ArrayList<String> itemsInStore = new ArrayList<>();

            // creates array of all items
            ArrayList<String> itemStrings = new ArrayList<>();
            String line;
            while ((line = itemReader.readLine()) != null) {
                itemStrings.add(line);
            }

            for (String item : itemStrings) {
                String storeToCheck = item.split(",")[0];
                if (storeToCheck.equals(storeName)) {
                    itemsInStore.add(item);
                }
            }

            String filename = storeName + "—Items.csv";

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            for (String lineToWrite : itemsInStore) {
                writer.println(lineToWrite);
            }
            System.out.println("Exported item file successfully");

            writer.close();
            itemReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting file");
        }
    }

    public static void viewCustomerShoppingCart() { // Prints customer shopping cart info for Sellers
        try {
            // Read through CSV file
            BufferedReader fmReader = new BufferedReader(new FileReader("FMCredentials.csv"));

            ArrayList<String> credentials = new ArrayList<>();

            // Add existing items to ArrayList;
            String line;
            while ((line = fmReader.readLine()) != null) {
                credentials.add(line);
            }

            fmReader.close();

            ArrayList<String> shoppingCart = new ArrayList<>();
            ArrayList<String> customerNames = new ArrayList<>();

            // loop through arraylist and find the correct account
            int credSize = credentials.size();
            for (int i = 0; i < credSize; i++) {
                String[] credentialsSplit = credentials.get(i).split(",");
                String shoppingCartLine = credentialsSplit[5];
                if (credentialsSplit[3].equals("buyer")) {
                    shoppingCart.add(shoppingCartLine);
                    customerNames.add(credentialsSplit[1]);
                }
            }

            ArrayList<String> cartInfoLine = new ArrayList<>();

            // Get amount of items in cart for each customer
            for (int i = 0; i < shoppingCart.size(); i++) {
                if (!shoppingCart.get(i).equals("x")) {
                    String[] cartItems = shoppingCart.get(i).split("~");
                    for (int j = 0; j < cartItems.length; j++) {
                        try {
                            String[] itemFields = cartItems[j].split("!");
                            String storeName = itemFields[0];
                            String itemName = itemFields[1];
                            String quantity = itemFields[3];
                            String price = itemFields[4];
                            String itemInfoLine = "Store Name: " + storeName + ", Item Name: " + itemName + ", Quantity: "
                                    + quantity + ", Price bought at: $" + price;
                            cartInfoLine.add(itemInfoLine);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            int y = 0;
            for (int i = 0; i < shoppingCart.size(); i++) {
                System.out.printf("Customer Name: %s\n", customerNames.get(i));
                String[] credentialsSplit = credentials.get(i).split(",");
                String[] cartLine = credentialsSplit[5].split("~");
                for (int j = 0; j < cartLine.length; j++) {
                    if (!cartLine[j].equals("x")) {
                        System.out.printf("  %s\n", cartInfoLine.get(y));
                        y++;
                    } else {
                        System.out.println("  Customer cart empty!");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public Store[] getStore() {
        Store[] userStores = new Store[this.stores.size()];
        for (int i = 0; i < this.stores.size(); i++) {
            userStores[i] = this.stores.get(i);
        }
        return userStores;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void deleteAccount() {
        String line;
        StringBuilder credentialsFile = new StringBuilder();
        StringBuilder storesFile = new StringBuilder();
        //StringBuilder itemsFile = new StringBuilder();
        ArrayList<String> itemsFileOutput = new ArrayList<>();
        try {
            // First remove user from credentials file
            BufferedReader bfrOne = new BufferedReader(new FileReader("FMCredentials.csv"));
            line = bfrOne.readLine();
            int counter = 0;
            while (line != null) {
                // Only saves account to reprint to the file if they don't have the email belonging to this account
                if (!email.equals(line.substring(0, line.indexOf(",")))) {
                    if (counter >= 1) {
                        credentialsFile.append("\n");
                    }

                    // Also deletes items from seller's stores from shopping carts
                    String[] splitLine = line.split(",");
                    String cart = splitLine[5];
                    if (cart.contains("~")) {
                        String[] cartItems = cart.split("~");
                        for (int i = 0; i < cartItems.length; i++) {
                            for (int j = 0; j < stores.size(); j++) {
                                if (cartItems[i].substring(0, cartItems[i].indexOf("!")).equals(stores.get(j).getStoreName())) {
                                    if (!cart.contains("~")) {
                                        cart = "x";
                                    }else if (cart.contains("~" + cartItems[i])) {
                                        cart = cart.replaceFirst("~" + cartItems[i], "");
                                    } else cart = cart.replaceFirst(cartItems[i] + "~", "");
                                }
                            }
                        }
                    } else if (cart.contains("!")) {
                        for (int i = 0; i < stores.size(); i++) {
                            if (cart.substring(0, cart.indexOf("!")).equals(stores.get(i).getStoreName())) {
                                cart = "x";
                            }
                        }
                    }
                    line = line.replaceAll(splitLine[5], cart);

                    credentialsFile.append(line);
                    counter++;
                }
                line = bfrOne.readLine();
            }
            bfrOne.close();
            PrintWriter pwOne = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            if (credentialsFile.length() != 0) {
                pwOne.println(credentialsFile);
            }
            pwOne.close();
        } catch (Exception e) {
            System.out.println("Error deleting user credentials!");
            e.printStackTrace();
        }
        try {
            // Second, remove all stores belonging to this owner from stores file
            BufferedReader bfrTwo = new BufferedReader(new FileReader("FMStores.csv"));
            line = bfrTwo.readLine();
            int counter = 0;
            while (line != null) {
                // Only saves stores that don't use this users email
                String[] splitLine = line.split(",");
                if (!email.equals(splitLine[1])) {
                    if (counter >= 1) {
                        storesFile.append("\n");
                    }
                    storesFile.append(line);
                    counter++;
                }
                line = bfrTwo.readLine();
            }
            bfrTwo.close();
            PrintWriter pwTwo = new PrintWriter(new FileOutputStream("FMStores.csv", false));
            if (storesFile.length() != 0) {
                pwTwo.println(storesFile);
            }
            pwTwo.close();
        } catch (Exception e) {
            System.out.println("Error deleting user stores!");
            e.printStackTrace();
        }
        try {
            // Third, remove all items belonging to this owner's stores from items file
            BufferedReader bfrThree = new BufferedReader(new FileReader("FMItems.csv"));
            String itemLine = "";
            while ((itemLine = bfrThree.readLine()) != null) {
                boolean keep = true;
                // Only saves items whose store doesn't match any of this owner's stores
                for (int i = 0; i < stores.size(); i++) {
                    String[] splitLine = itemLine.split(",");
                    if (stores.get(i).getStoreName().equals(splitLine[0])) {
                        keep = false;
                    }
                }
                if (keep) {
                    itemsFileOutput.add(itemLine);
                }
            }
            bfrThree.close();
            PrintWriter pwThree = new PrintWriter(new FileOutputStream("FMItems.csv", false));
            for (int i = 0; i < itemsFileOutput.size(); i++) {
                pwThree.println(itemsFileOutput.get(i));
            }
            pwThree.close();
        } catch (Exception e) {
            System.out.println("Error deleting user items!");
            e.printStackTrace();
        }
    }

    public int importItems(String fileName, Store[] stores) { // Adds imported items to stores
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(fileName));
            String line = "";
            int numberSuccess = 0;
            while ((line = bfr.readLine()) != null) {
                String[] splitLine = line.split(",");
                for (int i = 0; i < stores.length; i++) {
                    // Tests against all store names until one works
                    if (splitLine[0].equals(stores[i].getStoreName())) {
                        try {
                            stores[i].addItem(splitLine[1], splitLine[2],
                                    Integer.parseInt(splitLine[3]), Double.parseDouble(splitLine[4]));
                            numberSuccess++;
                            break;
                        } catch (Exception e) {

                        }
                    }
                }
            }
            return numberSuccess;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
