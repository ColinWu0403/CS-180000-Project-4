import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
/**
 * A Test class for the Store class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2022 -- Project 4</p>
 *
 * @author Andrei Deaconescu
 * @version Nov 12, 2022
 */
class StoreTest {
    private final PrintStream out = System.out;
    private ByteArrayOutputStream outputStream;
    private final Seller seller = new Seller("a", "a@bc.com", "ff");

    @BeforeEach
    public void setup() {                   // to check methods that print to the console
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void reset() {                  // resets files after testing
        try {
            FileWriter storeWriter = new FileWriter("FMItems.csv", false);
//            FileWriter statsWriter = new FileWriter("FMStats.csv", false);
            storeWriter.write("");
//            statsWriter.write("");

            storeWriter.close();
//            statsWriter.close();

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
    void getOwner() {
        Store input = new Store("a@bc.com", "astore");
        String output = input.getOwner();
        assertEquals("a@bc.com", output);
        seller.deleteStore(input);
    }

    @Test
    void getStoreName() {
        Store input = new Store("a@bc.com", "astore");
        String output = input.getStoreName();
        assertEquals("astore", output);
        seller.deleteStore(input);
    }

    @Test
    void getItems() {
        Store input = new Store("a@bc.com", "cool stuff");
        input.addItem("cool table", "a really cool table", 5, 499.99);
        input.addItem("really cool TV", "an EXTREMELY cool TV", 1,
                999999.99);

        String[] expectedNames = new String[] { "cool table", "really cool TV" };
                String[] expectedDescriptions = new String[] { "a really cool table", "an EXTREMELY cool TV" };
        int[] expectedQuantities = new int[] { 5, 1 };
        double[] expectedPrices = new double[] { 499.99, 999999.99 };

        ArrayList<Item> output = input.getItems();

        for (int i = 0; i < 1; i++) {
            assertEquals(output.get(i).getName(), expectedNames[i]);
            assertEquals(output.get(i).getDescription(),
                    expectedDescriptions[i]);
            assertEquals(output.get(i).getQuantity(),
                    expectedQuantities[i]);
            assertEquals(output.get(i).getPrice(), expectedPrices[i]);
        }
        seller.deleteStore(input);
     }


        @Test
        void addItem() {
            Store input = new Store("a@bc.com", "my store");
            input.addItem("my table", "MY table.", 1, 99999999.99);

            String expectedStore = "my store";
            String expectedName = "my table";
            String expectedDescription = "MY table.";
            int expectedQuantity = 1;
            double expectedPrice = 99999999.99;

            Item output = input.getItems().get(0);

            assertEquals(output.getStore(), expectedStore);
            assertEquals(output.getName(), expectedName);
            assertEquals(output.getDescription(), expectedDescription);
            assertEquals(output.getQuantity(), expectedQuantity);
            assertEquals(output.getPrice(), expectedPrice);

            seller.deleteStore(input);
        }

        @Test
        void printItems() {

            Store input = new Store("a@bc.com", "amazing stuff");
            input.addItem("amazingPad", "a tablet that can do everything", 4,
                    999.99);
            input.addItem("amazingPhone", "a phone as amazing as you", 10,
                    999.99);
            input.addItem("amazingPod", "a really amazing mp3 player", 8,
                    599.99);

            String expectedOutput = """
                1. amazingPad selling at 999.99
                2. amazingPhone selling at 999.99
                3. amazingPod selling at 599.99
                """;
            input.printItems();
            assertEquals(expectedOutput, outputStream.toString());
            seller.deleteStore(input);
        }

        @Test
        void printItemNames() {
            Store input = new Store("you@you.com", "more amazing stuff");
            input.addItem("amazingBook", "a laptop that's just the best'", 4,
                    999.99);
            input.addItem("amazingMac", "a PC that can cook and clean", 10,
                    999.99);
            input.addItem("amazingMacPro", "a PC that can also do homework", 8,
                    99999999.99);

            String expectedOutput = """
                (1)amazingBook
                (2)amazingMac
                (3)amazingMacPro
                """;
            input.printItemNames();

            assertEquals(expectedOutput, outputStream.toString());
            seller.deleteStore(input);
        }

        @Test
        void viewItem() {
            Store input = new Store("you@you.com", "your stuff");
            input.addItem("your table", "a table that's going to be yours", 4,
                    999.99);
            input.addItem("your house", "a portable house. yours, of course", 10,
                    999.99);
            input.addItem("your city", "you are the new mayor of a city!", 8,
                    599.99);


            String expectedOutput1 = "your table selling at 999.99. 4 in stock.\na table that's going to be yours\n";
            input.viewItem(1);
            assertEquals(expectedOutput1, outputStream.toString());
            outputStream.reset();

            String expectedOutput2 = "your house selling at 999.99. 10 in stock.\na portable house. yours, of course\n";
            input.viewItem(2);
            assertEquals(expectedOutput2, outputStream.toString());
            outputStream.reset();

            String expectedOutput3 = "your city selling at 599.99. 8 in stock.\nyou are the new mayor of a city!\n";
            input.viewItem(3);
            assertEquals(expectedOutput3, outputStream.toString());

            seller.deleteStore(input);
        }

        @Test
        void saveSale() {
            Store input = new Store("a@bc.com", "aa");
            Buyer buyer = new Buyer("jim", "jim@jim.jim", "ff", null, null);
            input.addItem("something", "some", 3, 9.99);
            boolean saleSaved = input.saveSale("jim", input.getItems().get(0), 2);

            if (input.getItems().get(0).getQuantity() != 1) {
                fail("error: sale not recorded");
            }
            assertTrue(saleSaved);
        }

//        @Test                    ------- Method returns null
//        void showSales() {
//            Store input = new Store("a@bc.com", "aa");
//            Buyer buyer = new Buyer("jim", "jim@jim.jim", "ff", null, null);
//            input.addItem("something", "some", 3, 9.99);
//            input.addItem("something else", "some 2", 5, 8.99);
//            boolean sale1Saved = input.saveSale("jim", input.getItems().get(0), 2);
//            boolean sale2Saved = input.saveSale("jim", input.getItems().get(1), 3);
//            assertTrue(sale1Saved);
//            assertTrue(sale2Saved);
//
//            ArrayList<String> sales = input.showSales();
//            System.out.println(Arrays.toString(sales.toArray()));
//        }

//        @Test                             ----- Static method not associated to any store ???
//        void showStats() {
//            Store input = new Store("a@bc.com", "aa");
//            Buyer buyer = new Buyer("jim", "jim@jim.jim", "ff", null, null);
//            input.addItem("something", "some", 3, 9.99);
//            input.addItem("something else", "some 2", 5, 8.99);
//            boolean sale1Saved = input.saveSale("jim", input.getItems().get(0), 2);
//            boolean sale2Saved = input.saveSale("jim", input.getItems().get(1), 3);
//            assertTrue(sale1Saved);
//            assertTrue(sale2Saved);
//
//            String[] expected = new String[] { "aa,jim,7,buyer", "aa,something,4,item",
//                    "aa,something else,3,item" };
//
//            ArrayList<String> actual = showStats();
//        }
    }
