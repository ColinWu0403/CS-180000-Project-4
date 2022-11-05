import java.io.*;
import java.util.Arrays;
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
                    System.out.println("Thanks for using furniture marketplace");
        }

        if (currentUser instanceof Buyer) {

        } else if (currentUser instanceof Seller) {

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
            } else if (checkExistingCredentials(newEmail, newUsername, "newAccount").equals("DuplicateEmail")) {
                System.out.println("Error: Username already exists");
            } else {
                creatingCredentials = false;
            }
        }

        System.out.print("Are you a Buyer or a Seller?\n(1)Buyer\n(2)Seller\n");
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
            Seller currentSeller = new Seller(newUsername, newEmail, newPassword);
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
            accountSearch = accountSearch.substring(1, accountSearch.length() - 1);
            if (!accountSearch.equals("noAccount")) {
                String[] accountDetails = accountSearch.split(", ");
                if (accountDetails[3].equals("buyer")) {
                    return new Buyer(accountDetails[1], accountDetails[0], accountDetails[2]);
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
}