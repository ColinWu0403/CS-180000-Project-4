import java.io.*;
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

        String loginResponse = "";  //User response to account options
        String buyerSellerResponse = "";    //User response to whether account is buyer or seller

        String signInEmail = "";            //User inputted email when signing in to an account
        String signInPassword = "";         //User inputted password when signing in to an account

        String newUsername = "";            //Username created by the user
        String newEmail = "";               //Email created by the user
        String newPassword = "";            //Password created by the user
        String newBuyerOrSeller = "";       //Contains whether the user will be a "buyer" or "seller"


        while (true) {
            loginResponse = scanner.nextLine();
            if (!loginResponse.equals("1") && !loginResponse.equals("2")) {
                System.out.println("Please enter a valid option");
            } else {
                break;
            }
        }
        if (loginResponse.equals("1")) {        //user chooses to sign in
            while (true) {
                System.out.print("Enter Email: ");
                signInEmail = scanner.nextLine();
                System.out.print("Enter Password: ");
                signInPassword = scanner.nextLine();
                if (existingCredentials(signInEmail, signInPassword, "signIn").equals("valid")) {

                    System.out.println("signed in");            //user has signed in and program goes to another class
                    break;

                } else {
                    System.out.println("No account found, try again");
                }
            }

        } else {                                //user chooses to create an account
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
                } else if (existingCredentials(newEmail, newUsername, "newAccount").equals("DuplicateEmail")) {
                    System.out.println("Error: Email already exists");
                } else if (existingCredentials(newEmail, newUsername, "newAccount").equals("DuplicateEmail")) {
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
                new Buyer(newUsername, newEmail, newPassword);
            } else {                                //new user is created that is a Seller
                newBuyerOrSeller = "seller";
                new Seller(newUsername, newEmail, newPassword);
            }

            try {                                   //writes the new user's account to the csv file
                PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("FMCredentials.csv"
                        , true)));
                printWriter.println(newEmail + "," + newUsername + "," + newPassword + "," + newBuyerOrSeller);
                printWriter.flush();
                printWriter.close();
            } catch (Exception e) {
                return;
            }



        }

    }

    /* Purpose: Returns a string to tell whether the user's inputted username or email already exists
    @param String email : string with the user inputted email
    @param String username : string with the user inputted username
    @param String purpose : contains a string with "newAccount" or "signIn"*/
    public static String existingCredentials(String email, String usernameOrPassword, String purpose) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
            String line = "";
            while ((line = bfr.readLine()) != null) {
                String[] currentLine = line.split(",");

                if (purpose.equals("signIn")) {
                    if (currentLine[0].equals(email) && currentLine[1].equals(usernameOrPassword)) {
                        return "valid";
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
            return "";
        } catch (Exception e) {
            return "";
        }
    }

}

/* TODO
1.still need to add back options and cancel options (is this required?)
2.make sure all the strings printed to the terminal make sense
3.call other functions that need to be called after this part is done
 */
