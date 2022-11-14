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
        } catch (IOException e) {}
        String newEmail = "userEmail";
        String newUsername = "newUsername";
        String purpose = "newAccount";
        String checkReturn = FurnitureMarketplace.checkExistingCredentials(newEmail, newUsername, purpose);
        Assertions.assertEquals(checkReturn, "DuplicateEmail");

        newEmail = "newEmail";
        newUsername = "username";
        purpose = "newAccount";
        checkReturn = FurnitureMarketplace.checkExistingCredentials(newEmail, newUsername, purpose);
        Assertions.assertEquals(checkReturn, "DuplicateUsername");
    }



}
