import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
/**
 * A Test class for the Seller class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2022 -- Project 4</p>
 *
 * @author Andrei Deaconescu
 * @version Nov 12, 2022
 */
class SellerTest {
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
    void createStore() {
        Seller input = new Seller("a", "a@bc.com", "ff");
        input.createStore(new Store("a@bc.com", "astore"));
        input.createStore(new Store("a@bc.com", "bstore"));

        String[] expectedNames = new String[] { "astore", "bstore" };
        String[] expectedOwners = new String[] { "a@bc.com", "a@bc.com" };

        Store[] stores = input.getStore();
        String[] actualNames = new String[] { stores[0].getStoreName(), stores[1].getStoreName() };
        String[] actualOwners = new String[] { stores[0].getOwner(), stores[1].getOwner() };

        for (int i = 0; i < 1; i++) {
            assertEquals(expectedNames[i], actualNames[i]);
            assertEquals(expectedOwners[i], actualOwners[i]);
        }
    }

    @Test
    void deleteStore() {
        Seller input = new Seller("a", "a@bc.com", "ff");
        Store astore = new Store("a@bc.com", "astore");
        Store bstore = new Store("a@bc.com", "bstore");
        input.createStore(astore);
        input.createStore(bstore);

        input.deleteStore(bstore);

        Store[] stores = input.getStore();
        for (int i = 0; i < stores.length; i++) {
            if (stores[i].getStoreName().equals(bstore.getStoreName())) {
                fail("Store was not deleted!");
            }
        }
    }

    @Test
    void exportPublishedItems() {
        Seller input = new Seller("a", "a@bc.com", "ff");
        Store aStore = new Store("a@bc.com", "astore");
        aStore.addItem("cool table", "really cool table", 4, 0.99);
        aStore.addItem("cool chair", "really cool chair", 5, 1.99);

        input.exportPublishedItems("astore");

        String[] expectedOutput = new String[] { "astore,cool table,really cool table,4,0.99",
                "astore,cool chair,really cool chair,5,1.99" };
        try {
            File file = new File("astore—Items.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<String> actualOutput = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                actualOutput.add(line);
            }
            for (int i = 0; i < actualOutput.size(); i++) {
                assertEquals(expectedOutput[i], actualOutput.get(i));
            }
            boolean deleted = file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void viewCustomerShoppingCart() {
        Seller input = new Seller("a", "a@bc.com", "ff");
        Store aStore = new Store("a@bc.com", "astore");
        input.createStore(aStore);
        aStore.addItem("cool blank", "a cool blank.", 99, 99.99);
        Buyer buyer = new Buyer("iBuy", "buy@buy.buy", "buy", null, null);
        buyer.addItem("astore,cool blank,a cool blank,99,99.99", "cool blank", 5);

        System.setOut(out);
        Seller.viewCustomerShoppingCart();
    }

    @Test
    void getEmail() {
        Seller input = new Seller("a", "a@bc.com", "ff");
        String expectedEmail = "a@bc.com";
        String actualEmail = input.getEmail();

        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void getName() {
        Seller input = new Seller("jim", "a@bc.com", "ff");
        String expectedName = "jim";
        String actualName = input.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    void setName() {
        Seller input = new Seller("jim", "a@bc.com", "ff");
        input.setName("gim");

        String expectedName = "gim";
        String actualName = input.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    void getPassword() {
        Seller input = new Seller("jim", "a@bc.com", "ff");
        String expected = "ff";
        String actual = input.getPassword();

        assertEquals(expected, actual);
    }

    @Test
    void getStore() {
        Seller input = new Seller("jim", "a@bc.com", "ff");
        Store store1 = new Store("jim", "jim's store");
        Store store2 = new Store("jim", "jim's store two");
        input.createStore(store1);
        input.createStore(store2);

        Store[] stores = input.getStore();
        String[] expected = new String[] { "jim's store", "jim's store two" };
        String[] actual = new String[] { stores[0].getStoreName(), stores[1].getStoreName() };

        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    void setPassword() {
        Seller input = new Seller("jim", "a@bc.com", "ff");
        input.setPassword("gg");

        String expected = "gg";
        String actual = input.getPassword();

        assertEquals(expected, actual);
    }

    @Test
    void deleteAccount() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("FMCredentials.csv"));
            Seller input = new Seller("jim", "a@bc.com", "ff");
            String confirmed = reader.readLine();
            input.deleteAccount();
            System.setOut(out);
            System.out.println(confirmed);
            // not finished
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void importItems() {
        try {
            File file = new File("toImport.csv");
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("toImport.csv")));
            writer.println("amazon,cool something,a cool something,4,6.99");
            writer.flush();
            writer.println("amazon,another cool something,a second cool something,6,7.99");
            writer.flush();
            writer.println("amazon2,yet another cool something,a third cool something,2,10.99");
            writer.flush();

            Seller input = new Seller("jim", "a@bc.com", "ff");
            Store amazon = new Store("a@bc.com", "amazon");
            Store amazon2 = new Store("a@bc.com", "amazon2");
            input.createStore(amazon);
            input.createStore(amazon2);
            input.importItems(file.getName(), new Store[] { amazon, amazon2 });
            Store[] stores = input.getStore();
            ArrayList<Item> amazonItems = stores[0].getItems();
            ArrayList<Item> amazon2Items = stores[1].getItems();

            String[] amazonNames = new String[] { amazonItems.get(0).getName(), amazonItems.get(1).getName() };
            String amazon2Name = amazon2Items.get(0).getName();

            String[] expected1 = new String[] { "cool something", "another cool something" };
            String expected2 = "yet another cool something";
            assertEquals(expected1[0], amazonNames[0]);
            assertEquals(expected1[1], amazonNames[1]);
            assertEquals(expected2, amazon2Name);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
