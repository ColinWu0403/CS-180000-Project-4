import java.io.*;
import java.util.*;

/**
 * Contains the main method and currently deals with user login
 *
 * @author Nathan Schneider
 * @version 2022 3-11
 */

public class FurnitureMarketplace {
    public static Item[] itemList;
    public static ArrayList<String> itemListStr;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Furniture Marketplace");

        boolean programIsRunning = true;
        while (programIsRunning) {

            System.out.println("(1) Sign In");
            System.out.println("(2) Create an account");
            System.out.println("(3) Exit");

            String[] inputOptions = {"1", "2", "3"};                            //Valid options for first user input
            String loginResponse = validUserResponse(scanner, inputOptions);    //User response to account options
            Object currentUser = null;                                          //object of Buyer or Seller for current user

            switch (loginResponse) {
                case "1" ->                //user chooses to sign in
                        currentUser = signInAccount(scanner);
                case "2" ->                //user chooses to create an account
                        currentUser = createAccount(scanner);
                case "3" -> {                 //user chooses to exit the program
                    programIsRunning = false;
                    System.out.println("Thank you for using Furniture Marketplace!");
                }
            }

            if (currentUser instanceof Buyer userB) {
                while (true) {
                    itemList = createItemList();
                    itemListStr = createItemListString();
                    if (itemList != null) {
                        printBuyerDashboard(itemList);
                    }

                    String[] choicesFromDashboard = {"1", "2", "3", "4", "5", "6", "7"};
                    String userChoiceFromDashboard = validUserResponse(scanner, choicesFromDashboard);

                    if (!buyerDashboardNavigation(scanner, userChoiceFromDashboard, userB).equals("repromptDashboard")) {
                        break;
                    }
                }
            } else if (currentUser instanceof Seller userS) {

                //createStoresList()        this method has to be made?
                //printSellerDashboard() this method has to be made
                while (true) {
                    System.out.println("""
                            \t\tSeller Dashboard
                            (1) Manage Stores
                            (2) Sales List
                            (3) Statistics Dashboard
                            (4) View Current Carts
                            (5) Manage Account
                            (6) Sign out""");
                    String[] choicesFromDashboard = {"1", "2", "3", "4", "5", "6"};
                    String userChoiceFromDashboard = validUserResponse(scanner, choicesFromDashboard);

                    if (!sellerDashboardNavigation(scanner, userChoiceFromDashboard, userS).equals("repromptDashboard")) {
                        break;
                    }
                }
            }
        }
    }

    public Item[] getItemList() {
        return itemList;
    }

    public static ArrayList<String> getItemListStr() {
        return itemListStr;
    }

    public static void setItemListStr(ArrayList<String> itemListStr) {
        FurnitureMarketplace.itemListStr = itemListStr;
    }

    public void setItemList(Item[] itemList) {
        this.itemList = itemList;
    }

    //creates an account that is appended to FMCredentials.csv and returns either a Buyer or Seller object
    public static Object createAccount(Scanner scanner) {
        String newUsername = "";            //Username created by the user
        String newEmail = "";               //Email created by the user
        String newPassword = "";            //Password created by the user
        String newBuyerOrSeller = "";       //Contains whether the user will be a "buyer" or "seller"
        String buyerSellerResponse = "";    //User response to whether account is buyer or seller

        boolean creatingCredentials = true; //Keeps prompting credentials until valid options are submitted
        while (creatingCredentials) {
            System.out.print("Username: ");
            newUsername = scanner.nextLine();
            System.out.print("Email: ");
            newEmail = scanner.nextLine();
            System.out.print("Password: ");
            newPassword = scanner.nextLine();

            if (newUsername.length() == 0 || newEmail.length() == 0 || newPassword.length() == 0) {
                System.out.println("Error: Ensure username, email, and password are entered");
            } else if (checkExistingCredentials(newEmail, newUsername, "newAccount").equals("DuplicateEmail")) {
                System.out.println("Error: Email already exists");
            } else if (checkExistingCredentials(newEmail, newUsername,
                    "newAccount").equals("DuplicateUsername")) {
                System.out.println("Error: Username already exists");
            } else {
                creatingCredentials = false;
            }
        }

        System.out.print("Are you a Buyer or a Seller?\n(1) Buyer\n(2) Seller\n");
        while (true) {
            buyerSellerResponse = scanner.nextLine();
            if (!buyerSellerResponse.equals("1") && !buyerSellerResponse.equals("2")) {
                System.out.println("Please enter a valid option");
            } else {
                break;
            }
        }

        Buyer currentBuyer = null;
        Seller currentSeller = null;
        if (buyerSellerResponse.equals("1")) {     //new user is created that is a Buyer
            newBuyerOrSeller = "buyer";
            currentBuyer = new Buyer(newUsername, newEmail, newPassword, null, null);
        } else {                                //new user is created that is a Seller
            newBuyerOrSeller = "seller";
            currentSeller = new Seller(newUsername, newEmail, newPassword);
        }

        try {                                   //writes the new user's account to the csv file
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("FMCredentials.csv"
                    , true)));


            printWriter.println(newEmail + "," + newUsername + "," + newPassword + "," + newBuyerOrSeller + ",x,x");
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newBuyerOrSeller.equals("buyer")) {
            return currentBuyer;
        } else if (newBuyerOrSeller.equals("seller")) {
            return currentSeller;
        }
        return null;
    }

    //checks if the user has an account with the email and password and returns that Buyer or Seller object
    public static Object signInAccount(Scanner scanner) {
        String signInEmail = "";            //User inputted email when signing in to an account
        String signInPassword = "";         //User inputted password when signing in to an account
        while (true) {
            System.out.print("Enter Email: ");
            signInEmail = scanner.nextLine();
            System.out.print("Enter Password: ");
            signInPassword = scanner.nextLine();
            String accountSearch = checkExistingCredentials(signInEmail, signInPassword, "signIn");
            if (!accountSearch.equals("noAccount")) {
                accountSearch = accountSearch.substring(1, accountSearch.length() - 1);
                String[] accountDetails = accountSearch.split(", ");
                if (accountDetails[3].equals("buyer")) {
                    return new Buyer(accountDetails[1], accountDetails[0], accountDetails[2], buyerDataArray(accountDetails[0], "hist"), buyerDataArray(accountDetails[0], "cart"));
                } else if (accountDetails[3].equals("seller")) {
                    return new Seller(accountDetails[1], accountDetails[0], accountDetails[2]);
                }
            } else {
                System.out.println("No account found, try again");
            }
        }
    }

    /* Purpose: Returns a string to tell whether the user's inputted username or email already exists
    @param String email : string with the user inputted email
    @param String username : string with the user inputted username
    @param String purpose : contains a string with "newAccount" or "signIn"*/
    public static String checkExistingCredentials(String email, String usernameOrPassword, String purpose) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
            String line = "";
            while ((line = bfr.readLine()) != null) {
                String[] currentLine = line.split(",");

                if (purpose.equals("signIn")) {
                    if (currentLine[0].equals(email) && currentLine[2].equals(usernameOrPassword)) {
                        return Arrays.toString(currentLine);
                    }
                } else if (purpose.equals("newAccount")) {
                    if (currentLine[1].equals(usernameOrPassword)) {
                        return "DuplicateUsername";
                    } else if (currentLine[0].equals(email)) {
                        return "DuplicateEmail";
                    }
                }
            }
            bfr.close();
            return "noAccount";
        } catch (Exception e) {
            return "";
        }
    }

    public static String validItemName(Scanner scanner) {
        boolean invalidItemName = true;
        String itemName = "";
        while (invalidItemName) {
            System.out.print("Enter the name for your item: ");
            itemName = scanner.nextLine();
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("FMItems.csv"));
                String line = "";
                int c = 0;
                while ((line = bfr.readLine()) != null) {
                    String[] itemInfo = line.split(",");
                    if (itemName.equals(itemInfo[1])) {
                        System.out.println("Error: This item name already exists");
                        c++;
                        break;
                    }
                }
                if (c == 0) {
                    invalidItemName = false;
                }
                bfr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemName;
    }

    /* Purpose: returns the response from the user if it is a valid response.
    @param String[] inputOptions : possible options that can be accepted the by the user*/
    public static String validUserResponse(Scanner scanner, String[] inputOptions) {
        String userResponse = "";  //User response to account options
        while (true) {
            userResponse = scanner.nextLine();
            for (String option : inputOptions) {
                if (userResponse.equals(option)) {
                    return userResponse;
                }
            }
            System.out.println("Please enter a valid option");
        }
    }


    /**
     * Returns a list of Item objects to be accessed throughout the program.
     */
    public static Item[] createItemList() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("FMItems.csv"));

            String store;
            String name;
            String description;
            int quantity;
            double price;
            Item item;
            ArrayList<Item> itemListing = new ArrayList<>();

            String line;
            while ((line = bfr.readLine()) != null) {
                String[] splitLine = line.split(",");
                store = splitLine[0];
                name = splitLine[1];
                description = splitLine[2];
                //It shouldn't be possible for incorrect data to exist in the csv
                quantity = Integer.parseInt(splitLine[3]);
                price = Double.parseDouble(splitLine[4]);
                item = new Item(store, name, description, quantity, price);
                itemListing.add(item);
            }
            Item[] itemList = new Item[itemListing.size()];
            for (int i = 0; i < itemListing.size(); i++) {
                itemList[i] = itemListing.get(i);
            }
            return itemList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns string representation of Item to be accessed throughout the program.
     */
    public static ArrayList<String> createItemListString() {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("FMItems.csv"));

            ArrayList<String> itemListStr = new ArrayList<>();

            String line;
            while ((line = bfr.readLine()) != null) {
                itemListStr.add(line);
            }

            bfr.close();
            return itemListStr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * A function used to Get user data from FMCredentials to be used in the Buyer constructor
     *
     * @param userEmail  User email to make sure you get the right user data
     * @param cartOrHist If Cart, Cart list is returned. If Hist, Purchase History list is returned
     * @return
     */
    public static ArrayList<String> buyerDataArray(String userEmail, String cartOrHist) {
        try {
            ArrayList<String> buyerData = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
            String line = "";
            while ((line = bfr.readLine()) != null) {
                String[] currentLine = line.split(",");
                if (currentLine[0].equals(userEmail)) { // first checks for user creds to give correct history
                    if (cartOrHist.equals("hist")) { // if hist, get Purchase history
                        if (currentLine[4].equals("")) {
                            return null;
                        } else {
                            String[] initialData = currentLine[4].split("~"); // Split each purchase
                            Collections.addAll(buyerData, initialData);
                        }
                    } else { // if cart, get cart
                        if (currentLine[5].equals("")) {
                            return null;
                        } else {
                            String[] initialData = currentLine[5].split("~"); // Split each purchase
                            Collections.addAll(buyerData, initialData);
                        }
                    }
                }
            }
            return buyerData;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prints Dashboard for buyer to view after logging in.
     *
     * @param itemList The Item list obtained from the createItemList method.
     */
    public static void printBuyerDashboard(Item[] itemList) {
        String[] outputOptions = {"(1) Select Product", "(2) View Cart", "(3) Search",
                "(4) Review Purchase History", "(5) Manage Account", "(6) View Statistics", "(7) Sign Out"};

        System.out.printf("\n%-8s Select Option %-12s Furniture Dashboard\n", "", "");
        int i = 0;
        while (true) {
            if (outputOptions.length <= itemList.length) {
                if (i < 7) {
                    System.out.printf("%-30s ||  ", outputOptions[i]);
                    System.out.printf("(%d) ", i);
                    itemList[i].printItem();
                } else if (i < itemList.length) {
                    System.out.printf("%-31s||  ", "");
                    System.out.printf("(%d) ", i);
                    itemList[i].printItem();
                } else {
                    break;
                }
            } else if (itemList.length < outputOptions.length) {
                if (i < itemList.length) {
                    System.out.printf("%-30s ||  ", outputOptions[i]);
                    System.out.printf("(%d) ", i);
                    itemList[i].printItem();
                } else if (i < 7) {
                    System.out.printf("%-30s ||  \n", outputOptions[i]);
                } else {
                    break;
                }
            }
            i++;
        }
    }

    public static String validStoreName(Scanner scanner) {
        boolean invalidStoreName = true;
        String storeName = "";
        while (invalidStoreName) {
            System.out.print("Enter the name for your store: ");
            storeName = scanner.nextLine();
            try {
                BufferedReader bfr = new BufferedReader(new FileReader("FMStores.csv"));
                String line = "";
                int c = 0;
                while ((line = bfr.readLine()) != null) {
                    String[] storeInfo = line.split(",");
                    if (storeName.equals(storeInfo[0])) {
                        System.out.println("Error: This store name already exists");
                        c++;
                        break;
                    }
                }
                if (c == 0) {
                    invalidStoreName = false;
                }
                bfr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Store created!");
        return storeName;
    }

    public static String buyerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Buyer currentUser) {
        switch (userChoiceFromDashboard) {
            case "1" -> { // Product Selection - Adds product to cart
                boolean itemSelectLoop = true;
                while (itemSelectLoop) {
                    System.out.println("Which Item would you like to select: ");
                    String[] validResponse = new String[itemList.length];
                    try {
                        // Create valid responses based on length of itemList
                        for (int i = 0; i < validResponse.length; i++) {
                            validResponse[i] = String.valueOf(i);
                        }
                        String itemNumStr = validUserResponse(scanner, validResponse); // require valid response
                        int itemNum = Integer.parseInt(itemNumStr);

                        if (itemList != null) {
                            String[] itemStrSplit = itemListStr.get(itemNum).split(","); // get itemList fields
                            String storeName = itemStrSplit[0];
                            String itemName = itemStrSplit[1];
                            String description = itemStrSplit[2];
                            String quantityAvailable = itemStrSplit[3];
                            String price = itemStrSplit[4];
                            System.out.println("\tItem information:");
                            System.out.printf("Store Name: %s\nItem Name: %s\nDescription: %s\nQuantity Available: %s\nPrice: $%s\n",
                                    storeName, itemName, description, quantityAvailable, price);

                            System.out.print("(1) Add item to shopping cart\n(2) Cancel\n");
                            String[] validCartResponse = {"1", "2"};
                            System.out.print("Input: ");
                            String shoppingCartOption = validUserResponse(scanner, validCartResponse);

                            if (shoppingCartOption.equals("1")) { // Add to cart
                                while (true) {
                                    int quantityToPurchase = 0;
                                    System.out.print("How many would you like to add to cart: ");
                                    quantityToPurchase = scanner.nextInt();
                                    scanner.nextLine();
                                    if ((quantityToPurchase > Integer.parseInt(quantityAvailable)) || (quantityToPurchase <= 0)) {
                                        System.out.println("Error: Invalid quantity input");
                                        continue;
                                    }
                                    String itemToAdd = storeName + "!" + itemName + "!" + description + "!"
                                            + quantityToPurchase + "!" + price;
                                    currentUser.addItem(itemToAdd, itemName, quantityToPurchase);
                                    System.out.println("Item added to cart!");
                                    break;
                                }
                            }
                            itemSelectLoop = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error item doesn't exist");
                    }
                }
            }
            case "2" -> { // view cart options
                if (currentUser.getCart().get(0).equals("x")) {
                    System.out.println("Cart Empty");
                } else {
                    System.out.println("Cart: ");
                    System.out.println(currentUser.printCart());
                    System.out.printf("""
                            \t\tManage Cart
                            (1) Checkout
                            (2) Remove Item
                            (3) Return
                            """);
                    String[] inputOptions = {"1", "2", "3"};
                    String userResponse = validUserResponse(scanner, inputOptions);

                    switch (userResponse) {
                        case "1": // 2-1 DONE
                            currentUser.checkout();
                            //save sales
                            for (int i = 0; i < currentUser.getCart().size(); i++) {
                                String[] splitLine = currentUser.getCart().get(i).split("!");
                                Item saleItem = new Item(splitLine[0], splitLine[1], splitLine[2], Integer.parseInt(splitLine[3]), Double.parseDouble(splitLine[4]));
                                Store.saveSale(currentUser.getEmail(), saleItem, Integer.parseInt(splitLine[3]));
                            }
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add("x");
                            currentUser.setCart(temp);

                            System.out.println("Checkout Successful!");

                            break;
                        case "2":
                            System.out.println("Input the number of the item you would like to remove: ");
                            ArrayList<String> removeCartInputOptions = new ArrayList<>();

                            for (int i = 0; i < currentUser.getCart().size(); i++) {
                                removeCartInputOptions.add(Integer.toString(i + 1));
                            }
                            String[] cartInputOptions = new String[removeCartInputOptions.size()];
                            for (int i = 0; i < removeCartInputOptions.size(); i++) {
                                cartInputOptions[i] = removeCartInputOptions.get(i);
                            }
                            String deleteCartResponse = validUserResponse(scanner, cartInputOptions);
                            currentUser.removeItemFromCart(Integer.parseInt(deleteCartResponse));
                            System.out.println("Item removed successfully");
                            break;
                        case "3":

                            break;
                    }
                }
            }
            case "3" -> {
                //Search items by name, store, description, sort Quantity/Price
                System.out.print("""
                        \tSearch By:
                        (1) Product name
                        (2) Store
                        (3) Description
                        \tSort By:
                        (4) Price
                        (5) Quantity
                        """);
                String[] validInputs = {"1", "2", "3", "4", "5"};
                String itemOption = validUserResponse(scanner, validInputs);

                switch (itemOption) {                        // search by product name
                    case "1" -> {
                        ArrayList<Item> output = new ArrayList<>();
                        System.out.println("Enter the item name");
                        String itemName = scanner.nextLine();
                        itemList = createItemList();
                        for (int i = 0; i < itemList.length; i++) {
                            if (itemList[i].getName().contains(itemName)) {
                                output.add(itemList[i]);
                            }
                        }
                        if (output.size() == 0) {
                            System.out.println("Error: No items match that name");
                        } else {
                            for (int i = 0; i < output.size(); i++) {
                                output.get(i).printItem();
                            }
                            System.out.println("Type (exit) to exit");
                            String filler = scanner.nextLine();
                        }
                    }
                    case "2" -> {       // search by store
                        System.out.println("Enter the store name");
                        String storeName = scanner.nextLine();
                        ArrayList<Item> output = new ArrayList<>();
                        itemList = createItemList();
                        for (int i = 0; i < itemList.length; i++) {
                            if (itemList[i].getStore().contains(storeName)) {
                                output.add(itemList[i]);
                            }
                        }

                        if (output.size() == 0) {
                            System.out.println("Error: No stores match that name");
                        } else {
                            for (int i = 0; i < output.size(); i++) {
                                output.get(i).printItem();
                            }
                            System.out.println("Type (exit) to exit");
                            String filler = scanner.nextLine();
                        }
                    }
                    case "3" -> {           //search by description
                        System.out.println("Enter text to search for in description");
                        String searchDescription = scanner.nextLine();
                        ArrayList<Item> output = new ArrayList<>();
                        itemList = createItemList();
                        for (int i = 0; i < itemList.length; i++) {
                            if (itemList[i].getDescription().contains(searchDescription)) {
                                output.add(itemList[i]);
                            }
                        }

                        if (output.size() == 0) {
                            System.out.println("Error: No descriptions match that search");
                        } else {
                            for (int i = 0; i < output.size(); i++) {
                                output.get(i).printItem();
                            }
                            System.out.println("Type (exit) to exit");
                            String filler = scanner.nextLine();
                        }
                    }
                    case "4" -> {               //sort by price
                        itemList = createItemList();
                        ArrayList<Double> prices = new ArrayList<>();
                        ArrayList<Item> output = new ArrayList<>();
                        for (int i = 0; i < itemList.length; i++) {
                            prices.add(itemList[i].getPrice());
                        }
                        Collections.sort(prices);
                        prices.sort(Collections.reverseOrder());
                        for (int i = 0; i < prices.size(); i++) {
                            for (int j = 0; j < itemList.length; j++) {
                                if (prices.get(i) == itemList[j].getPrice()) {
                                    output.add(itemList[j]);
                                }
                            }
                        }
                        for (int i = 0; i < output.size(); i++) {
                            output.get(i).printItem();
                        }
                        System.out.println("Type (exit) to exit");
                        String filler = scanner.nextLine();
                        break;
                    }
                    case "5" -> {               //sory by quantity
                        itemList = createItemList();
                        ArrayList<Integer> quantity = new ArrayList<>();
                        ArrayList<Item> output = new ArrayList<>();
                        for (int i = 0; i < itemList.length; i++) {
                            quantity.add(itemList[i].getQuantity());
                        }
                        Collections.sort(quantity);
                        for (int i = 0; i < quantity.size(); i++) {
                            for (int j = 0; j < itemList.length; j++) {
                                if (quantity.get(i) == itemList[j].getQuantity()) {
                                    output.add(itemList[j]);
                                }
                            }
                        }
                        for (int i = 0; i < output.size(); i++) {
                            output.get(i).printItem();
                        }
                        System.out.println("Type (exit) to exit");
                        String filler = scanner.nextLine();
                        break;
                    }
                }
            }
            case "4" -> { // View or Export Purchase History
                System.out.print("(1) View Purchase History\n(2) Export Purchase History\n");
                String[] validInputs = {"1", "2"};
                String purchaseOption = validUserResponse(scanner, validInputs);
                //make sure hte formatting looks good and check if it is empty

                if (purchaseOption.equals("1")) {
                    try {
                        System.out.println("\t\tPurchase History:"); // review Purchase History
                        ArrayList<String> purchaseHistory = Buyer.showPurchaseHistory(currentUser.getEmail());
                        ArrayList<String> purchaseHistoryOutput = new ArrayList<>();

                        if (purchaseHistory != null) {
                            for (int i = 0; i < purchaseHistory.size(); i++) {
                                String[] splitLine = purchaseHistory.get(i).split("!");
                                double totalCost = Double.parseDouble(splitLine[3]) * Double.parseDouble(splitLine[4]);

                                System.out.printf("(%d)%-2sStore: %s\n" +
                                                "%-5sItem bought: %s\n" +
                                                "%-5sDescription: %s\n" +
                                                "%-5sQuantity bought = %s\n" +
                                                "%-5sTotal Cost = $%.2f\n", i + 1, "", splitLine[0], "", splitLine[1], "",
                                        splitLine[2], "", splitLine[3], "", totalCost);
                            }
                        } else {
                            System.out.println("Error: This account has no purchases");
                        }
                    } catch (Exception e) {
                        System.out.println("Error: Purchase History does not exist");
                    }
                } else if (purchaseOption.equals("2")) {
                    Buyer.exportPurchaseHistory(currentUser.getEmail());
                }
            }
            case "5" -> {                                      // Manage Account
                System.out.println("""
                        \t\tManage Account
                        (1) Change Username
                        (2) Change Password
                        (3) Delete Account
                        (4) Return""");
                String[] validInputs = {"1", "2", "3", "4"};
                String response = validUserResponse(scanner, validInputs);

                switch (response) {
                    case "1" -> {                       // Change Username
                        String newUsername;
                        while (true) {
                            System.out.println("Enter new username: ");
                            newUsername = scanner.nextLine();
                            if (checkExistingCredentials(null, newUsername,
                                    "newAccount").equals("DuplicateUsername")) {
                                System.out.println("Username already taken. Try again");
                            } else {
                                currentUser.setName(newUsername);
                                break;
                            }
                        }

                        try {
                            File file = new File("FMCredentials.csv");
                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            ArrayList<String> lines = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                lines.add(line);
                            }

                            reader.close();

                            FileOutputStream fos = new FileOutputStream(file, false);
                            PrintWriter writer = new PrintWriter(fos);

                            for (int i = 0; i < lines.size(); i++) {
                                String userLine = lines.get(i);
                                String email = userLine.split(",")[0];

                                if (currentUser.getEmail().equals(email)) {
                                    String oldUsername = userLine.split(",")[1];
                                    lines.set(i, userLine.replaceAll(oldUsername, newUsername));
                                }
                            }

                            for (String s : lines) {
                                writer.println(s);
                            }

                            writer.close();
                        } catch (Exception e) {
                            System.out.println("Error: Account NOT updated");
                        }
                    }
                    case "2" -> {       //change password
                        System.out.println("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        currentUser.setPassword(newPassword);

                        try {
                            File file = new File("FMCredentials.csv");
                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            ArrayList<String> lines = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                lines.add(line);
                            }

                            reader.close();

                            FileOutputStream fos = new FileOutputStream(file, false);
                            PrintWriter writer = new PrintWriter(fos);

                            for (int i = 0; i < lines.size(); i++) {
                                String userLine = lines.get(i);
                                String email = userLine.split(",")[0];

                                if (currentUser.getEmail().equals(email)) {
                                    String oldPassword = userLine.split(",")[2];
                                    lines.set(i, userLine.replaceAll(oldPassword, newPassword));
                                }
                            }

                            for (String s : lines) {
                                writer.println(s);
                            }

                            writer.close();
                        } catch (Exception e) {
                            System.out.println("Error: Account NOT updated");
                        }
                    }
                    case "3" -> {               //delete account
                        currentUser.deleteAccount();
                        System.out.println("Deleted Account");
                        return "AccountDeleted";

                    }
                    case "4" -> {               //return

                    }
                }
            }
            case "6" -> {                       // view statistics

            }
            case "7" -> {                   //// Sign Out
                System.out.println("Signing Out!");
                return "Sign out";
            }
        }
        return "repromptDashboard";
    }

    public static String sellerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Seller currentUser)
            throws IOException {
        switch (userChoiceFromDashboard) {
            case "1" -> {                                              //Manage stores
                boolean continueManageStore = true;
                while (continueManageStore) {
                    System.out.println("""
                            \t\tManage Stores
                            (1) Manage Catalogues
                            (2) Create Store
                            (3) Delete Store
                            (4) Return to Dashboard""");
                    String[] optionOneChoices = {"1", "2", "3", "4"};
                    String manageStoreResponse = validUserResponse(scanner, optionOneChoices);

                    switch (manageStoreResponse) {
                        case "1":                           //1-1- Manage catalogues
                            System.out.println("\tStores owned by: " + currentUser.getName());
                            Store[] currentUserStores = currentUser.getStore();
                            ArrayList<String> numberOptions = new ArrayList<>();
                            if (currentUserStores.length == 0) {
                                System.out.println("Error: You have no stores");
                                break;
                            }
                            for (int i = 0; i < currentUserStores.length; i++) {
                                numberOptions.add(Integer.toString((i + 1)));
                                System.out.println("(" + (i + 1) + ") " + currentUserStores[i].getStoreName());
                            }
                            System.out.print("What store would you like to modify: ");
                            String[] manageCatalogueOptions = new String[numberOptions.size()];
                            for (int i = 0; i < numberOptions.size(); i++) {
                                manageCatalogueOptions[i] = numberOptions.get(i);
                            }

                            String manageCatalogueResponse = validUserResponse(scanner, manageCatalogueOptions);

                            Store currentStore = currentUserStores[Integer.parseInt(manageCatalogueResponse) - 1];
                            System.out.println("\tCurrent Store: " + currentStore.getStoreName());
                            System.out.println("""
                                    (1) Add Product
                                    (2) Edit Product
                                    (3) Export Product File
                                    (4) Delete Product
                                    (5) Import Product File
                                    (6) Return to Dash""");
                            String[] editCatalogueOptions = {"1", "2", "3", "4", "5", "6"};
                            String editCatalogueResponse = validUserResponse(scanner, editCatalogueOptions);
                            switch (editCatalogueResponse) {
                                case "1":               //1-1-1 Add product to store : DONE

                                    String itemName = validItemName(scanner);
                                    System.out.print("Enter Item Description: ");
                                    String itemDescription = scanner.nextLine();
                                    String itemQuantity = "0";
                                    while (true) {
                                        System.out.print("Enter item quantity: ");
                                        try {
                                            itemQuantity = scanner.nextLine();
                                            if (Integer.parseInt(itemQuantity) <= 0) {
                                                throw new NumberFormatException();
                                            }

                                        } catch (NumberFormatException e) {
                                            System.out.println("Error: Please enter a valid number");
                                            continue;
                                        }
                                        break;
                                    }
                                    String itemPrice = "0.00";
                                    while (true) {
                                        System.out.print("Enter item price (ex. 49.99): ");
                                        try {
                                            double doubleItemPrice = scanner.nextDouble();
                                            itemPrice = Double.toString(doubleItemPrice);
                                            if (((doubleItemPrice * 100) % 1 != 0) || (doubleItemPrice < 0)) {
                                                throw new InputMismatchException();
                                            }
                                            scanner.nextLine();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Error: Please enter a number with two decimals (ex. 49.99)");
                                            scanner.nextLine();
                                            continue;
                                        }
                                        break;
                                    }
                                    currentStore.addItem(itemName, itemDescription, Integer.parseInt(itemQuantity),
                                            Double.parseDouble(itemPrice));

                                    break;
                                case "2":               //1-1-2 Edit the product information : DONE
                                    ArrayList<String> numberOfProducts = new ArrayList<>();
                                    System.out.println("\tProducts available in " + currentStore.getStoreName());
                                    if (currentStore.getItems().size() == 0) {
                                        System.out.println("Error: You have no products in this store");
                                        break;
                                    }
                                    for (int i = 0; i < currentStore.getItems().size(); i++) {
                                        numberOfProducts.add(Integer.toString((i + 1)));
                                    }
                                    if (!currentStore.printItemNames().equals("Error")) {
                                        String[] productSelectionOptions = new String[numberOfProducts.size()];
                                        for (int i = 0; i < currentStore.getItems().size(); i++) {
                                            productSelectionOptions[i] = numberOfProducts.get(i);
                                        }

                                        System.out.print("What product would you like to modify: ");
                                        String productSelectionResponse = validUserResponse(scanner, productSelectionOptions);
                                        Item currentItem = currentStore.getItems().get(Integer.parseInt(productSelectionResponse) - 1);


                                        boolean continueEditItem = true;
                                        while (continueEditItem) {
                                            System.out.println("""
                                                    (1) Edit item name
                                                    (2) Edit item description
                                                    (3) Edit item quantity
                                                    (4) Edit item price
                                                    (5) Return to Dash""");
                                            String[] editProductOptions = {"1", "2", "3", "4", "5"};
                                            System.out.print("What piece of product information would you like to modify: ");
                                            String editProductResponse = validUserResponse(scanner, editProductOptions);
                                            switch (editProductResponse) {
                                                case "1" -> {       //1-1-2-1 Edit item name : DONE
                                                    String newItemName = validItemName(scanner);
                                                    currentItem.changeField("name", newItemName);
                                                }
                                                case "2" -> {       //1-1-2-2 Edit item description : DONE
                                                    System.out.print("Enter new item description: ");
                                                    String newItemDescription = scanner.nextLine();
                                                    currentItem.changeField("description", newItemDescription);
                                                }
                                                case "3" -> {       //1-1-2-3 Edit item quantity : DONE
                                                    String newItemQuantity = "0";
                                                    while (true) {
                                                        System.out.print("Enter new item quantity: ");
                                                        try {
                                                            newItemQuantity = scanner.nextLine();
                                                            if (Integer.parseInt(newItemQuantity) <= 0) {
                                                                throw new NumberFormatException();
                                                            }
                                                        } catch (NumberFormatException e) {
                                                            System.out.println("Error: Please enter a number");
                                                            continue;
                                                        }
                                                        break;
                                                    }
                                                    currentItem.changeField("quantity", newItemQuantity);
                                                }
                                                case "4" -> {       //1-1-2-4 Edit item price : DONE
                                                    String newItemPrice = "0.00";
                                                    while (true) {
                                                        System.out.print("Enter new item price (ex. 49.99): ");
                                                        try {
                                                            double doubleItemPrice = scanner.nextDouble();
                                                            newItemPrice = Double.toString(doubleItemPrice);
                                                            if (((doubleItemPrice * 100) % 1 != 0) || (doubleItemPrice < 0)) {
                                                                throw new InputMismatchException();
                                                            }
                                                            scanner.nextLine();
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Error: Please enter a number with two decimals (ex. 49.99)");
                                                            scanner.nextLine();
                                                            continue;
                                                        }
                                                        break;
                                                    }
                                                    currentItem.changeField("price", newItemPrice);
                                                }
                                                case "5" ->       //1-1-2-5 Return to dash with loop: DONE
                                                        continueEditItem = false;
                                            }
                                        }
                                    }
                                    break;
                                case "3":               //1-1-3 Export Product File
                                    currentUser.exportPublishedItems(currentStore.getStoreName());
                                    break;
                                case "4":               //1-1-4 Delete the product
                                    ArrayList<String> numberOfProductsToDelete = new ArrayList<>();
                                    for (int i = 0; i < currentStore.getItems().size(); i++) {
                                        numberOfProductsToDelete.add(Integer.toString((i + 1)));
                                    }
                                    if (!currentStore.printItemNames().equals("Error")) {
                                        String[] productDeleteOptions = new String[numberOfProductsToDelete.size()];
                                        for (int i = 0; i < currentStore.getItems().size(); i++) {
                                            productDeleteOptions[i] = numberOfProductsToDelete.get(i);
                                        }

                                        System.out.print("What product would you like to Delete: ");
                                        String productToDeleteResponse = validUserResponse(scanner, productDeleteOptions);
                                        Item currentItem = currentStore.getItems().get(Integer.parseInt(productToDeleteResponse) - 1);
                                        currentItem.deleteItem();
                                        System.out.println("Deleted Item");
                                    }
                                    break;
                                case "5":               //1-1-5 Import product file
                                    System.out.print("Enter name of file: ");
                                    String filename = scanner.nextLine();
                                    int numberOfFiles = currentUser.importItems(filename, currentUserStores);
                                    if (numberOfFiles == -1) {
                                        System.out.println("Error: Incorrect format, no items added");
                                    } else {
                                        System.out.println("Imported " + numberOfFiles + " files successfuly");
                                    }


                                    break;
                                case "6":               //1-1-6 Return to dashboard

                                    break;
                            }

                            break;

                        case "2":                 //1-2 Create a store : DONE
                            String storeName = validStoreName(scanner);
                            currentUser.createStore(new Store(currentUser.getEmail(), storeName));
                            break;

                        case "3":                 //1-3 Delete a store : DONE
                            Store[] currentUserStoresToDelete = currentUser.getStore();
                            ArrayList<String> numberOptionsForStores = new ArrayList<>();
                            if (currentUserStoresToDelete.length == 0) {
                                System.out.println("Error: You have no stores");
                                break;
                            }
                            System.out.println("\tStores owned by: " + currentUser.getName());
                            for (int i = 0; i < currentUserStoresToDelete.length; i++) {
                                numberOptionsForStores.add(Integer.toString((i + 1)));
                                System.out.println("(" + (i + 1) + ")" + currentUserStoresToDelete[i].getStoreName());
                            }
                            System.out.print("What store would you like to delete: ");
                            String[] deleteStoreOptions = new String[numberOptionsForStores.size()];
                            for (int i = 0; i < numberOptionsForStores.size(); i++) {
                                deleteStoreOptions[i] = numberOptionsForStores.get(i);
                            }

                            String deleteStoreResponse = validUserResponse(scanner, deleteStoreOptions);
                            currentStore = currentUserStoresToDelete[Integer.parseInt(deleteStoreResponse) - 1];
                            System.out.println(currentStore.getStoreName() + " has been deleted");
                            currentUser.deleteStore(currentStore);
                            break;

                        case "4":                 //1-4 Return to the dashboard : DONE
                            continueManageStore = false;
                            break;
                    }
                }
            }
            case "2" -> {                                               //View Sales List
                Store[] currentUserStores = currentUser.getStore();
                ArrayList<String> numberOptions = new ArrayList<>();
                if (currentUserStores.length == 0) {
                    System.out.println("Error: You have no stores");
                    break;
                }
                for (int i = 0; i < currentUserStores.length; i++) {
                    numberOptions.add(Integer.toString((i + 1)));
                    System.out.println("(" + (i + 1) + ") " + currentUserStores[i].getStoreName());
                }
                System.out.println("Which store's sale history would you like to view?");
                String[] manageCatalogueOptions = new String[numberOptions.size()];
                for (int i = 0; i < numberOptions.size(); i++) {
                    manageCatalogueOptions[i] = numberOptions.get(i);
                }
                String manageCatalogueResponse = validUserResponse(scanner, manageCatalogueOptions);
                Store currentStore = currentUserStores[Integer.parseInt(manageCatalogueResponse) - 1];
                ArrayList<String> sales = currentUserStores[Integer.parseInt(manageCatalogueResponse) - 1].showSales();
                if (sales == null) {
                    System.out.println("Error: This store has no sale history");
                } else {
                    System.out.println("\tSale History for " + currentStore.getStoreName());
                    for (int i = 0; i < sales.size(); i++) {
                        String[] saleInfo = sales.get(i).split("!");
                        System.out.printf("%-25s  %-25s  %-25s  %-25s\n",
                                "Customer: " + saleInfo[0], "Product: " + saleInfo[1], "Quantity bought: " + saleInfo[2],
                                "Price bought at: " + saleInfo[3]);
                    }
                }
                System.out.println();
            }
            case "3" -> {                                                //View Statistics Dashboard
                Store[] currentUserStores = currentUser.getStore();
                ArrayList<String> numberOptions = new ArrayList<>();
                if (currentUserStores.length == 0) {
                    System.out.println("Error: You have no stores");
                    break;
                }
                for (int i = 0; i < currentUserStores.length; i++) {
                    numberOptions.add(Integer.toString((i + 1)));
                    System.out.println("(" + (i + 1) + ") " + currentUserStores[i].getStoreName());
                }
                System.out.println("Which store's statistics would you like to view?");
                String[] manageCatalogueOptions = new String[numberOptions.size()];
                for (int i = 0; i < numberOptions.size(); i++) {
                    manageCatalogueOptions[i] = numberOptions.get(i);
                }
                String manageCatalogueResponse = validUserResponse(scanner, manageCatalogueOptions);
                Store currentStore = currentUserStores[Integer.parseInt(manageCatalogueResponse) - 1];
                System.out.println("What would you like to view?\n(1) Buyers\n(2) Items");
                String buyerOrItem;
                if (validUserResponse(scanner, new String[]{"1", "2"}).equals("1")) {
                    buyerOrItem = "buyer";
                } else buyerOrItem = "item";
                ArrayList<String> stats = new ArrayList<>();
                System.out.println("Would you like the results sorted?\n(1) Yes\n(2) No");
                if (validUserResponse(scanner, new String[]{"1", "2"}).equals("1")) {
                    stats = Store.showSortedStats(currentStore.getStoreName(), buyerOrItem);
                } else stats = Store.showStats(currentStore.getStoreName(), buyerOrItem);
                for (int i = 0; i < stats.size(); i++) {
                    String[] splitLine = stats.get(i).split(",");
                    if (buyerOrItem.equals("buyer")) {
                        System.out.printf("%-20s %-20s\n", "Customer: " + splitLine[0], "Items Purchased = " + splitLine[1]);
                    } else {
                        System.out.printf("%-25s %-25s\n", "Itemname: " + splitLine[0], "Number of Sales = " + splitLine[1]);
                    }
                }
            }
            case "4" -> {                                                //View Current Carts
                Seller.viewCustomerShoppingCart();
            }
            case "5" -> {                                                //Manage Account
                boolean continueManageAccount = true;
                while (continueManageAccount) {
                    System.out.println("""
                            \t\tManage Account
                            (1) Edit Account
                            (2) Delete Account
                            (3) Return""");
                    String manageAccountResponse = validUserResponse(scanner, new String[]{"1", "2", "3"});

                    switch (manageAccountResponse) {
                        case "1" -> {                           //5-1 Edit Account
                            System.out.println("""
                                    \t\tManage Account
                                    (1) Change Username
                                    (2) Change Password
                                    (3) Return""");
                            String editAccountResponse = validUserResponse(scanner, new String[]{"1", "2", "3"});

                            switch (editAccountResponse) {
                                case "1" -> {           //5-1-1 Change username
                                    String newUsername;
                                    while (true) {
                                        System.out.print("Enter new username: ");
                                        newUsername = scanner.nextLine();
                                        if (checkExistingCredentials(null, newUsername,
                                                "newAccount").equals("DuplicateUsername")) {
                                            System.out.println("Username already taken. Try again");
                                        } else {
                                            currentUser.setName(newUsername);
                                            break;
                                        }
                                    }

                                    try {
                                        File file = new File("FMCredentials.csv");
                                        BufferedReader reader = new BufferedReader(new FileReader(file));
                                        ArrayList<String> lines = new ArrayList<>();
                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            lines.add(line);
                                        }

                                        reader.close();

                                        FileOutputStream fos = new FileOutputStream(file, false);
                                        PrintWriter writer = new PrintWriter(fos);

                                        for (int i = 0; i < lines.size(); i++) {
                                            String userLine = lines.get(i);
                                            String email = userLine.split(",")[0];

                                            if (currentUser.getEmail().equals(email)) {
                                                String oldUsername = userLine.split(",")[1];
                                                lines.set(i, userLine.replaceAll(oldUsername, newUsername));
                                            }
                                        }

                                        for (String s : lines) {
                                            writer.println(s);
                                        }

                                        writer.close();
                                    } catch (Exception e) {
                                        System.out.println("Error: Account NOT updated");
                                    }
                                }

                                case "2" -> {           //5-1-2 Change Password
                                    System.out.print("Enter new password: ");
                                    String newPassword = scanner.nextLine();
                                    currentUser.setPassword(newPassword);

                                    try {
                                        File file = new File("FMCredentials.csv");
                                        BufferedReader reader = new BufferedReader(new FileReader(file));
                                        ArrayList<String> lines = new ArrayList<>();
                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            lines.add(line);
                                        }

                                        reader.close();

                                        FileOutputStream fos = new FileOutputStream(file, false);
                                        PrintWriter writer = new PrintWriter(fos);

                                        for (int i = 0; i < lines.size(); i++) {
                                            String userLine = lines.get(i);
                                            String email = userLine.split(",")[0];

                                            if (currentUser.getEmail().equals(email)) {
                                                String oldPassword = userLine.split(",")[2];
                                                lines.set(i, userLine.replaceAll(oldPassword, newPassword));
                                            }
                                        }

                                        for (String s : lines) {
                                            writer.println(s);
                                        }

                                        writer.close();
                                    } catch (Exception e) {
                                        System.out.println("Error: Account NOT updated");
                                    }

                                }
                                case "3" -> {           //5-1-3 Return

                                }
                            }
                        }
                        case "2" -> {                           //5-2 Delete Account
                            currentUser.deleteAccount();
                            System.out.println("Deleted Account");
                            return "AccountDeleted";
                        }
                        case "3" -> {                           //5-3 Return to Dashboard Account : DONE
                            continueManageAccount = false;
                        }
                    }
                }
            }
            case "6" -> {
                System.out.println("Signing Out!");
                return "Sign out";
            }
        }
        return "repromptDashboard";
    }
}
