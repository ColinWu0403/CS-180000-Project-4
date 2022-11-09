import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * Contains the main method and currently deals with user login
 *
 * @author Nathan Schneider
 * @version 2022 3-11
 */

public class FurnitureMarketplace {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Furniture Marketplace");

        System.out.println("(1) Sign In");
        System.out.println("(2) Create an account");
        System.out.println("(3) Exit");

        String[] inputOptions = {"1", "2", "3"};                            //Valid options for first user input
        String loginResponse = validUserResponse(scanner, inputOptions);    //User response to account options
        Object currentUser = null;                                          //object of Buyer or Seller for current user

        switch (loginResponse) {
            case "1" ->            //user chooses to sign in
                    currentUser = signInAccount(scanner);
            case "2" ->           //user chooses to create an account
                    currentUser = createAccount(scanner);
            case "3" ->           //user chooses to exit the program
                    System.out.println("Thank you for using Furniture Marketplace!");
        }

        if (currentUser instanceof Buyer) {
            Item[] itemList;
            itemList = createItemList();

            while (true) {
                printBuyerDashboard(Objects.requireNonNull(itemList));

                String[] choicesFromDashboard = {"1", "2", "3", "4", "5", "6", "7"};
                String userChoiceFromDashboard = validUserResponse(scanner, choicesFromDashboard);

                switch (userChoiceFromDashboard) {
                    case "1":         //Buyer selects Product
                        System.out.println("Placeholder");  //Product code here
                        break;
                    case "2":        //Buyer Looks at their cart
                        System.out.println("Placeholder"); // Cart code here
                        break;
                    case "3":        //Buyer Narrows their search results
                        System.out.println("Placeholder"); // Search code here
                        break;
                    case "4":         //Buyer Sorts by price or quantity
                        System.out.println("Placeholder"); // Sort code here
                        break;
                    case "5":         //Buyer Reviews their purchase history
                        System.out.println("Placeholder"); // Statistics code here
                        break;
                    case "6":         //Buyer Manages their account
                        System.out.println("Placeholder"); // Management code here
                        break;
                    case "7":         //Buyer Logs out
                        System.out.println("Thank you for using Furniture Marketplace!");
                        System.out.println("Logging Out..."); // Logout Logic here
                        return;
                }
            }
        } else if (currentUser instanceof Seller) {

            //createStoresList()        this method has to be made?
            //printSellerDashboard() this method has to be made

            while (true) {
                String[] choicesFromDashboard = {"1", "2", "3", "4", "5", "6"};
                String userChoiceFromDashboard = validUserResponse(scanner, choicesFromDashboard);

                switch (userChoiceFromDashboard) {
                    case "1":        //Seller selects to manage their store(s)
                        System.out.println("Placeholder");
                        break;
                    case "2":         //Seller looks at the sales list
                        System.out.println("Placeholder");
                        break;
                    case "3":         //Seller looks at the statistics dashboard
                        System.out.println("Placeholder");
                        break;
                    case "4":         //Seller looks at the seller cart view
                        System.out.println("Placeholder");
                        break;
                    case "5":         //Seller chooses the manage their account
                        System.out.println("Placeholder");
                        break;
                    case "6":         //Seller chooses to log out
                        System.out.println("Thank you for using Furniture Marketplace!");
                        System.out.println("Logging Out..."); // Logout Logic here
                        return;
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

        if (buyerSellerResponse.equals("1")) {     //new user is created that is a Buyer
            newBuyerOrSeller = "buyer";
            Buyer currentBuyer = new Buyer(newUsername, newEmail, newPassword);
        } else {                                //new user is created that is a Seller
            newBuyerOrSeller = "seller";
            try {
                Seller currentSeller = new Seller(newUsername, newEmail, newPassword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {                                   //writes the new user's account to the csv file
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("FMCredentials.csv"
                    , true)));
            printWriter.println(newEmail + "," + newUsername + "," + newPassword + "," + newBuyerOrSeller);
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            return null;
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
                    return new Buyer(accountDetails[1], accountDetails[0], accountDetails[2]);
                } else if (accountDetails[3].equals("seller")) {
                    try {
                        return new Seller(accountDetails[1], accountDetails[0], accountDetails[2]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
                    if (currentLine[0].equals(email)) {
                        return "DuplicateEmail";
                    } else if (currentLine[1].equals(usernameOrPassword)) {
                        return "DuplicateUsername";
                    }
                }
            }
            bfr.close();
            return "noAccount";
        } catch (Exception e) {
            return "";
        }
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
     * Prints Dashboard for buyer to view after logging in.
     *
     * @param itemList The Item list obtained from the createItemList method.
     */
    public static void printBuyerDashboard(Item[] itemList) {
        String[] outputOptions = {"(1) Select Product", "(2) View Cart", "(3) Search", "(4) Sort",
                "(5) Review Purchase History", "(6) Manage Account", "(7) Sign Out"};

            System.out.printf("\n%-8s Select Option %-12s Furniture Dashboard\n", "", "");
            int i = 0;
            while (true) {
                if (outputOptions.length <= itemList.length) {
                    if (i < 7) {
                        System.out.printf("%-30s ||  ", outputOptions[i]);
                        itemList[i].printItem();
                    } else if (i < itemList.length) {
                        System.out.printf("%-31s||  ", "");
                        itemList[i].printItem();
                    } else {
                        break;
                    }
                } else if (itemList.length < outputOptions.length) {
                    if (i < itemList.length) {
                        System.out.printf("%-30s ||  ", outputOptions[i]);
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
    }
