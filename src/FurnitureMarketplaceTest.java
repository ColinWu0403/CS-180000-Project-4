import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A Test class for the FurnitureMarketplace class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2022 -- Project 4</p>
 *
 * @author Nathan Schneider
 * @version Nov 12, 2022
 */
class FurnitureMarketplaceTest {
    private final PrintStream out = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setup() {                   // to check methods that print to the console
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void reset() {                  // resets files after testing
        try {
            FileWriter itemWriter = new FileWriter("FMItems.csv", false);
            FileWriter storeWriter = new FileWriter("FMStores.csv", false);
            itemWriter.write("");
            storeWriter.write("");

            itemWriter.close();
            storeWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void closeReader() {
        System.setOut(out);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkExistingCredentials() {
        try {
            //Create initial user
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.println("userEmail,username,password,buyer,x,x");
            pw.close();
        } catch (IOException e) {
        }
        String priorAccount = "userEmail,username,password,buyer,x,x";
        //Checks the error if the new users email already exists as another users email
        String newEmail = "userEmail";
        String newUsername = "newUsername";
        String purpose = "newAccount";
        String checkReturn = FurnitureMarketplace.checkExistingCredentials(newEmail, newUsername, purpose);
        Assertions.assertEquals(checkReturn, "DuplicateEmail");
        //Checks the error if the new users password already exists as another users password
        newEmail = "newEmail";
        newUsername = "username";
        purpose = "newAccount";
        checkReturn = FurnitureMarketplace.checkExistingCredentials(newEmail, newUsername, purpose);
        Assertions.assertEquals(checkReturn, "DuplicateUsername");
        //Checks that no error is found and the return is that no duplicate information exists
        newEmail = "newEmail";
        newUsername = "newUsername";
        purpose = "newAccount";
        checkReturn = FurnitureMarketplace.checkExistingCredentials(newEmail, newUsername, purpose);
        Assertions.assertEquals(checkReturn, "noAccount");
        //Checks if the user can successfully sign in
        String signInEmail = "userEmail";
        String signInPassword = "password";
        purpose = "signIn";
        checkReturn = FurnitureMarketplace.checkExistingCredentials(signInEmail, signInPassword, purpose);
        String[] priorAccountList = priorAccount.split(",");
        Assertions.assertEquals(checkReturn, Arrays.toString(priorAccountList));
        //Checks if the user can fails to sign in
        signInEmail = "newEmail";
        signInPassword = "incorrectPassword";
        purpose = "signIn";
        checkReturn = FurnitureMarketplace.checkExistingCredentials(signInEmail, signInPassword, purpose);
        Assertions.assertEquals(checkReturn, "noAccount");

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMItems.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {

        }
    }

    //Confirms that the createItemList returns an arrayList of items from the requested store
    @Test
    void createItemList() {
        Store input = new Store("new@gmail.com", "myStore");
        Item item1 = new Item("input", "itemName", "description", 4, 5.55);
        input.addItem("itemName", "description", 4, 5.55);
        Item item2 = new Item("input", "Table", "very good table", 10, 39.99);
        input.addItem("Table", "very good table", 10, 39.99);

        //Checks if an item list can be retrieved from FMItems.csv
        Item[] expectedOutputList = {item1, item2};
        Item[] actualOutputList = FurnitureMarketplace.createItemList();

        String expectedOutput = "";
        String actualOutput = "";
        for (int i = 0; i < expectedOutputList.length; i++) {
            expectedOutput = expectedOutput + expectedOutputList[i].getName();
            actualOutput = actualOutput + actualOutputList[i].getName();
        }

        Assertions.assertEquals(expectedOutput, actualOutput);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMItems.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    @Test
    void createItemListString() {
        Store input = new Store("new@gmail.com", "myStore");
        Item item1 = new Item("input", "itemName", "description", 4, 5.55);
        input.addItem("itemName", "description", 4, 5.55);
        Item item2 = new Item("input", "Table", "very good table", 10, 39.99);
        input.addItem("Table", "very good table", 10, 39.99);

        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("myStore,itemName,description,4,5.55");
        expectedOutput.add("myStore,Table,very good table,10,39.99");

        ArrayList<String> actualOutput = FurnitureMarketplace.createItemListString();
        Assertions.assertEquals(expectedOutput.toString(), actualOutput.toString());
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMItems.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    @Test
    void buyerDataArray() {
        try {
            //Create initial user
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.println("userEmail,username,password,buyer,store!itemname!description!" +
                    "20!4.00,Walmart!table!good table!10!4.00");
            pw.close();
        } catch (IOException e) {
        }
        //Check if the cart items can be returned from the inputted uesr's email
        String actualOutput = FurnitureMarketplace.buyerDataArray("userEmail", "cart").toString();
        String expectedOutput = "[Walmart!table!good table!10!4.00]";

        Assertions.assertEquals(expectedOutput, actualOutput);
        //ewqrqwerwqr,qwerwqerwqer,qwerqwer,buyer,adsf!adsf!fasdf!20!4.00,adsf!adsf!fasdf!10!4.00

        //Checks if the purchaseHistory items can be returned from the inputted uesr's email
        actualOutput = FurnitureMarketplace.buyerDataArray("userEmail", "hist").toString();
        expectedOutput = "[store!itemname!description!20!4.00]";

        Assertions.assertEquals(expectedOutput, actualOutput);

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    void printBuyerDashboard() {

    }


}
