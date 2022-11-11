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

    //    public Item[] getItemList() {
//        return itemList;
//    }
//
//    public void setItemList(Item[] itemList) {
//        this.itemList = itemList;
//    }
//
//    public FurnitureMarketplace(Item[] itemList) {
//        this.itemList = itemList;
//    }

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
                case "1" -> {                //user chooses to sign in
                    currentUser = signInAccount(scanner);
                }
                case "2" -> {                //user chooses to create an account
                    currentUser = createAccount(scanner);
                }
                case "3" -> {                 //user chooses to exit the program
                    programIsRunning = false;
                    System.out.println("Thank you for using Furniture Marketplace!");
                }
            }

            if (currentUser instanceof Buyer userB) {
                itemList = createItemList();
                while (true) {
                    printBuyerDashboard(itemList);

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
            printWriter.println(newEmail + "," + newUsername + "," + newPassword + "," + newBuyerOrSeller + ",,");
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
                            String[] initialData = currentLine[4].split("@"); // Split each purchase
                            for (int i = 0; i < initialData.length; i++) {
                                buyerData.add(initialData[i]);
                            }
                        }
                    } else { // if cart, get cart
                        if (currentLine[5].equals("")) {
                            return null;
                        } else {
                            String[] initialData = currentLine[5].split("@"); // Split each purchase
                            for (int i = 0; i < initialData.length; i++) {
                                buyerData.add(initialData[i]);
                            }
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
                "(4) Review Purchase History", "(5) Manage Account", "(6) Sign Out"};

        System.out.printf("\n%-8s Select Option %-12s Furniture Dashboard\n", "", "");
        int i = 0;
        while (true) {
            if (outputOptions.length <= itemList.length) {
                if (i < 6) {
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
                } else if (i < 6) {
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
        return storeName;
    }

    public static String buyerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Buyer currentUser) {
        switch (userChoiceFromDashboard) {
            case "1" -> { // Product Selection - Adds product to cart
                System.out.println("Which Item would you like to select");
                int itemNum = Integer.parseInt(scanner.nextLine());

                String itemListStr = Arrays.toString(new Item[]{itemList[itemNum]});
                System.out.println(itemListStr);
                // Work in progress
            }
            case "2" -> {
                // view cart options
                if (currentUser.getCart().isEmpty()) {
                    System.out.println("Cart Empty");
                } else {
                    System.out.println("Cart: ");
                    System.out.println(currentUser.printCart());
                    System.out.printf("""
                            \t\tManage Cart
                            (1) Checkout\n
                            (2) Remove Item\n
                            """);
                }
            }
            case "3" -> {
                //Search items by name, store, description, sort Quantity/Price
                System.out.printf("(1) Search by product name\n" +
                        "(2) Search by store\n" +
                        "(3) Search by price\n");
                int itemOption = Integer.parseInt(scanner.nextLine());

                switch (itemOption) {
                    case 1 -> {
                        System.out.println("Enter the item name");
                        String itemName = scanner.nextLine();

//                        for (int i = 0; i < itemList.length; i++) {
                        System.out.println(Arrays.toString(itemList));
//                        }
                    }
                    case 2 -> {
                        System.out.println("Placeholder"); //Search items by name, store, description, sort Quantity/Price
                    }
                }
            }
            case "4" -> { // View or Export Purchase History
                System.out.printf("(1) View Purchase History\n(2) Export Purchase History\n");
                int purchaseOption = Integer.parseInt(scanner.nextLine());

                if (purchaseOption == 1) {
                    System.out.println("Purchase History:"); // review Purchase History
                    ArrayList<String> purchaseHistory = Buyer.showPurchaseHistory(currentUser.getEmail());

                    for (int i = 1; i < purchaseHistory.size(); i++) {
                        System.out.printf("(%d) %s\n", i, purchaseHistory.get(i));
                    }
                } else if (purchaseOption == 2) {
                    Buyer.exportPurchaseHistory(currentUser.getEmail());
                }
            }
            case "5" -> {                                      // Manage Account
                System.out.println("""
                                \t\tManage Account
                                (1) Change Username
                                (2) Change Password
                                (3) Return""");
                String[] validInputs = {"1", "2", "3"};
                String response = validUserResponse(scanner, validInputs);

                switch (response) {
                    case "1" -> {                       // Change Username

                        String username;
                        while (true) {
                            System.out.println("Enter username:");
                            username = scanner.nextLine();
                            if (checkExistingCredentials(null, username,
                                    "newAccount").equals("DuplicateUsername")) {
                                System.out.println("Username already taken. Try again");
                            } else {
                                currentUser.setName(username);
                                break;
                            }
                        }

                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(
                                    "FMCredentials.csv"));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(
                                    "FMCredentials.csv"));

                            ArrayList<String> lines = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                lines.add(line);
                            }

                            for (int i = 0; i < lines.size(); i++) {
                                String userLine = lines.get(i);
                                String email = userLine.split(",")[0];

                                if (currentUser.getEmail().equals(email)) {
                                    String oldUsername = userLine.split(",")[1];
                                    lines.set(i, userLine.replaceAll(oldUsername, username));
                                }
                                writer.write(lines.get(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "2" -> {
                        System.out.println("Enter password");
                        String password = scanner.nextLine();
                        currentUser.setPassword(password);

                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(
                                    "FMCredentials.csv"));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(
                                    "FMCredentials.csv"));

                            ArrayList<String> lines = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                lines.add(line);
                            }

                            for (int i = 0; i < lines.size(); i++) {
                                String userLine = lines.get(i);
                                String email = userLine.split(",")[0];

                                if (currentUser.getEmail().equals(email)) {
                                    String oldPassword = userLine.split(",")[2];
                                    lines.set(i, userLine.replaceAll(oldPassword, password));
                                }
                                writer.write(lines.get(i));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case "6" -> { // Sign Out
                System.out.println("Signing Out!");
                return "Sign out";
            }
        }
        return "repromptDashboard";
    }

    public static String sellerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Seller currentUser)
            throws IOException {
        switch (userChoiceFromDashboard) {
            case "1":                                               //Manage stores
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
                            Store[] currentUserStores = currentUser.getStore();
                            ArrayList<String> numberOptions = new ArrayList<>();
                            if (currentUserStores.length == 0) {
                                System.out.println("Error: You have no stores");
                                break;
                            }
                            System.out.println("\tStores owned by: " + currentUser.getName());
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
                            boolean continueCatalogueStore = true;
                            while (continueCatalogueStore) {
                                System.out.println("\tCurrent Store: " + currentStore.getStoreName());
                                System.out.println("""
                                        (1) Add Product
                                        (2) Edit Product
                                        (3) Export Product File
                                        (4) Delete Product
                                        (5) Return to Dash""");
                                String[] editCatalogueOptions = {"1", "2", "3", "4", "5"};
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
                                                Integer.parseInt(itemQuantity);
                                            } catch (NumberFormatException e) {
                                                System.out.println("Error: Please enter a number");
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
                                                if ((doubleItemPrice * 100) % 1 != 0) {
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
                                                    case "1":       //1-1-2-1 Edit item name : DONE
                                                        String newItemName = validItemName(scanner);
                                                        currentItem.changeField("name", newItemName);
                                                        break;
                                                    case "2":       //1-1-2-2 Edit item description : DONE
                                                        System.out.print("Enter new item description: ");
                                                        String newItemDescription = scanner.nextLine();
                                                        currentItem.changeField("description", newItemDescription);
                                                        break;
                                                    case "3":       //1-1-2-3 Edit item quantity : DONE
                                                        String newItemQuantity = "0";
                                                        while (true) {
                                                            System.out.print("Enter new item quantity: ");
                                                            try {
                                                                newItemQuantity = scanner.nextLine();
                                                                Integer.parseInt(newItemQuantity);
                                                            } catch (NumberFormatException e) {
                                                                System.out.println("Error: Please enter a number");
                                                                continue;
                                                            }
                                                            break;
                                                        }
                                                        currentItem.changeField("quantity", newItemQuantity);
                                                        break;
                                                    case "4":       //1-1-2-4 Edit item price : DONE
                                                        String newItemPrice = "0.00";
                                                        while (true) {
                                                            System.out.print("Enter new item price (ex. 49.99): ");
                                                            try {
                                                                double doubleItemPrice = scanner.nextDouble();
                                                                newItemPrice = Double.toString(doubleItemPrice);
                                                                if ((doubleItemPrice * 100) % 1 != 0) {
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
                                                        break;
                                                    case "5":       //1-1-2-5 Return to dash with loop: DONE
                                                        continueEditItem = false;
                                                        break;
                                                }
                                            }
                                        }
                                        break;
                                    case "3":               //1-1-3 Export Product File

                                        break;
                                    case "4":               //1-1-4 Delete the product : DONE
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
                                            currentStore.getItems().remove(currentItem);
                                            System.out.println("Deleted Item");
                                        }
                                        break;
                                    case "5":               //1-1-5 Return to dashboard with loop : DONE
                                        continueCatalogueStore = false;
                                        break;
                                }
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
            case "2":                                                //View Sales List

                break;
            case "3":                                                //View Statistics Dashboard

                break;
            case "4":                                                //View Current Carts

                break;
            case "5":                                                //Manage Account : DONE
                boolean continueManageAccount = true;
                while (continueManageAccount) {
                    System.out.println("""
                            \t\tManage Account
                            (1) Edit Account
                            (2) Delete Account
                            (3) Return""");
                    String manageAccountResponse = validUserResponse(scanner, new String[]{"1", "2", "3"});

                    switch (manageAccountResponse) {
                        case "1":                           //5-1 Edit Account : DONE
                            System.out.println("""
                                    \t\tManage Account
                                    (1) Change Username
                                    (2) Change Password
                                    (3) Return""");
                            String editAccountResponse = validUserResponse(scanner, new String[]{"1", "2", "3"});

                            switch (editAccountResponse) {
                                case "1":            //5-1-1 Change username : DONE
                                    String newUsername = "";
                                    while (true) {
                                        System.out.print("New Username: ");
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
                                        BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
                                        ArrayList<String> lines = new ArrayList<>();
                                        String line = "";
                                        while ((line = bfr.readLine()) != null) {
                                            lines.add(line);
                                        }

                                        for (int i = 0; i < lines.size(); i++) {
                                            String userLine = lines.get(i);
                                            String email = userLine.split(",")[0];

                                            if (currentUser.getEmail().equals(email)) {
                                                String oldUsername = userLine.split(",")[1];
                                                lines.set(i, userLine.replaceAll(oldUsername, newUsername));
                                            }
                                        }

                                        bfr.close();
                                        FileOutputStream fos = new FileOutputStream("FMCredentials.csv", false);
                                        PrintWriter pw = new PrintWriter(fos);
                                        for (String s : lines) {
                                            pw.println(s);
                                        }
                                        pw.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //will need to update csv's
                                    break;
                                case "2":           //5-1-2 Change Password : DONE
                                    System.out.print("New Password: ");
                                    String newPassword = scanner.nextLine();
                                    currentUser.setPassword(newPassword);
                                    //update FMCredentials.csv
                                    try {
                                        ArrayList<String> lines = new ArrayList<>();
                                        BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
                                        String line = "";
                                        while ((line = bfr.readLine()) != null) {
                                            String[] splitLine = line.split(",");
                                            lines.add(line);
                                        }
                                        for (int i = 0; i < lines.size(); i++) {
                                            String userLine = lines.get(i);
                                            String email = userLine.split(",")[0];
                                            if (currentUser.getEmail().equals(email)) {
                                                String oldPassword = userLine.split(",")[2];
                                                lines.set(i, userLine.replaceAll(oldPassword, newPassword));
                                            }
                                        }
                                        bfr.close();
                                        FileOutputStream fos = new FileOutputStream("FMCredentials.csv", false);
                                        PrintWriter pw = new PrintWriter(fos);
                                        for (String s : lines) {
                                            pw.println(s);
                                        }
                                        pw.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("Password Successfully changed");
                                    break;
                                case "3":           //5-1-3 Return no loop: DONE

                                    break;
                            }
                            break;
                        case "2":                           //5-2 Delete Account : DONE
                            currentUser.deleteAccount();
                            System.out.println("Deleted Account");
                            return "AccountDeleted";
                        case "3":                           //5-3 Return to Dashboard Account with loop: DONE
                            continueManageAccount = false;
                            break;
                    }
                }
                break;
            case "6":                                                       //Sign out : DONE
                System.out.println("Thank you for using Furniture Marketplace");
                return "Sign out";
        }
        return "repromptDashboard";
    }

}
