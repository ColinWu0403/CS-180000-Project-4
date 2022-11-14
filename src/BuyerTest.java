import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A Test class for the Buyer class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2022 -- Project 4</p>
 *
 * @author Andrei Deaconescu
 * @version Nov 13, 2022
 */
class BuyerTest {                   // some methods only work if content is added manually to the file
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
    void purchaseItem() {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.purchaseItem("neat desks,neat desk,a neat desk,4,29.99");
        String expected = "neat desks,neat desk,a neat desk,4,29.99";
        String actual = input.getPurchaseHistory().get(0);

        assertEquals(expected, actual);
    }

    @Test
    void exportPurchaseHistory() {
        try {
            //Create initial user
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.println("a@bc.com,jim,ff,buyer,neat desks!neat desk!a neat desk!4!29.99~neat desks!real neat desk!" +
                    "a really neat desk!1!299.99,x");
            pw.close();

            //Checks that file is created with the correct purchase history information inside.
            Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
            input.purchaseItem("neat desks,neat desk,a neat desk,4,29.99");
            input.purchaseItem("neat desks,real neat desk,a really neat desk,1,299.99");
            Buyer.exportPurchaseHistory("a@bc.com");

            File file = new File("a@bc.comPurchaseHistory.csv");
            boolean r = file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> actual = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                actual.add(line);
            }
            String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                    "neat desks!real neat desk!a really neat desk!1!299.99"};


            assertEquals(expected[0], actual.get(0));
            assertEquals(expected[1], actual.get(1));

            reader.close();
            boolean test = file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    @Test
    void showPurchaseHistory() {
        try {
            //Create initial user
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.println("a@bc.com,jim,ff,buyer,neat desks!neat desk!a neat desk!4!29.99~neat desks!real neat desk!" +
                    "a really neat desk!1!299.9,x");
            pw.close();
        } catch (IOException e) {

        }

        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99", "neat desks!real neat desk!" +
                "a really neat desk!1!299.9"};
        ArrayList<String> actual = Buyer.showPurchaseHistory("a@bc.com");

        assert actual != null;
        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    @Test
    void showItemsInCart() {
        try {
            //Create initial user
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.println("a@bc.com,jim,ff,buyer,x,neat desks!neat desk!a neat desk!4!29.99~neat desks!real neat" +
                    " desk!a really neat desk!1!299.99");
            pw.close();
        } catch (IOException e) {

        }
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);

        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99"};
        ArrayList<String> actual = Buyer.showItemsInCart("a@bc.com");

        assert actual != null;
        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    //Searches all purchase history for each store in the FMStores.csv and returns how many items the inputted email
    //has purchased from each store.
    @Test
    void storesFromBuyerProducts() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("FMStores.csv"));
        writer.println("storeName,a123456@bc.com,ChairLover@gmail.com!awesome chair!5!39.99~" +
                "ChairLover@gmail.com!awesome chair!5!39.99");
        writer.flush();

        Buyer input = new Buyer("jimmy neutron", "a123456@bc.com", "qwertyuiop", null, null);

        writer.close();

        ArrayList<String> stores = input.storesFromBuyerProducts("ChairLover@gmail.com");

        String expected = "storeName,10";
        String actual = stores.get(0);

        assertEquals(expected, actual);

    }

    @Test
    void sortStoresFromBuyerProducts() {
    }

    @Test
    void storesFromProductsSold() {
    }

    @Test
    void sortStoresProductsSold() {
    }

    @Test
    void addItem() throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("FMCredentials.csv");
        PrintWriter writer = new PrintWriter(new PrintWriter(fos));
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);

        writer.println("a@bc.com,jim,ff,x,x");
        writer.close();

        input.addItem("neat desks!neat desk!a neat desk!4!29.99", "neat desk", 2);

        // incomplete
    }

    @Test
    void getName() {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        String expected = "jim";
        String actual = input.getName();

        assertEquals(expected, actual);
    }

    @Test
    void getEmail() {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        String expected = "a@bc.com";
        String actual = input.getEmail();

        assertEquals(expected, actual);
    }

    @Test
    void getPassword() {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        String expected = "ff";
        String actual = input.getPassword();

        assertEquals(expected, actual);
    }

    @Test
    void setName() {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.setName("foo");
        String expected = "foo";
        String actual = input.getName();

        assertEquals(expected, actual);
    }

    @Test
    void setCart() {
        ArrayList<String> cart = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!4!29.99");
        cart.add("neat desks!real neat desk!a really neat desk!1!299.99");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.setCart(cart);
        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99"};
        ArrayList<String> actual = input.getCart();

        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));
    }

    @Test
    void setPassword() {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.setPassword("gg");

        String expected = "gg";
        String actual = input.getPassword();

        assertEquals(expected, actual);
    }

    @Test
    void getCart() {
        ArrayList<String> cart = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!4!29.99");
        cart.add("neat desks!real neat desk!a really neat desk!1!299.99");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.setCart(cart);

        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99"};
        ArrayList<String> actual = input.getCart();

        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));
    }

    @Test
    void printCart() {
        ArrayList<String> cart = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!2!25.00");

        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.setCart(cart);

        String expected = "(1) neat desk from neat desks; Quantity: 2; Total Price: $50.00\n";
        String actual = input.printCart();

        assertEquals(expected, actual);
    }

    @Test
    void checkout() {
        ArrayList<String> cart = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!4!29.99");
        cart.add("neat desks!real neat desk!a really neat desk!1!299.99");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.setCart(cart);
        input.checkout();
        ArrayList<String> actual = input.getPurchaseHistory();
        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99"};

        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));
    }

    //Checks that when an item is removed from the buyer's shopping cart, the item is also removed from
    //the FMCredentials.csv file that displays the items currently in the user's cart.
    @Test
    void removeItemFromCart() throws FileNotFoundException {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        FileOutputStream fos = new FileOutputStream("FMCredentials.csv", false);
        PrintWriter writer = new PrintWriter(new PrintWriter(fos));

        writer.println("a@bc.com,jim,ff,buyer,x,John's Chairs!nameofItem!awesome chair!5!39.99~Brad's Tables!itemName!ok table!3!29.99");
        writer.close();
        input.getCart().remove(0);
        input.getCart().add("John's Chairs!nameofItem!awesome chair!5!39.99");
        input.getCart().add("Brad's Tables!itemName!ok table!3!29.99");

        input.removeItemFromCart(1);
        String expectedOutput = "a@bc.com,jim,ff,buyer,x,Brad's Tables!itemName!ok table!3!29.99";
        String actualOutput = "";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("FMCredentials.csv"));
            actualOutput = bfr.readLine();
        } catch (IOException e) {
        }

        assertEquals(expectedOutput, actualOutput);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("FMCredentials.csv", false));
            pw.print("");
            pw.close();
        } catch (IOException e) {
        }
    }

    @Test
    void csvTemporaryStorage() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new PrintWriter("FMCredentials.csv"));
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);

        writer.println("a@bc.com,jim,ff,x,x");
        writer.println("brad@bc.com,brad,11,x,x");
        writer.println("jake@bc.com,jake,33,x,x");
        writer.flush();
        writer.close();

        ArrayList<String> expected = new ArrayList<>();

        expected.add("brad@bc.com,brad,11,x,x");
        expected.add("jake@bc.com,jake,33,x,x");

        ArrayList<String> actual = input.csvTemporaryStorage();

        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    void getPurchaseHistory() {
        ArrayList<String> cart = new ArrayList<>();
        cart.add("John's Chairs!awesome chair!5!39.99");

        Buyer input = new Buyer("jake", "jake@bc.com", "ff", null, null);
        input.setCart(cart);
        input.checkout();

        ArrayList<String> expected = new ArrayList<>();
        expected.add("John's Chairs!awesome chair!5!39.99");

        ArrayList<String> actual = input.getPurchaseHistory();

        assertEquals(expected, actual);
    }

    @Test
    void deleteAccount() {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("FMCredentials.csv")));
            writer.println("a@bc.com,jim,ff,buyer,,x");
            writer.flush();
            BufferedReader reader = new BufferedReader(new FileReader("FMCredentials.csv"));
            BufferedReader reader2 = new BufferedReader(new FileReader("FMCredentials.csv"));
            Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);

            String actual = reader.readLine();
            input.deleteAccount();
            String actual2 = reader2.readLine();
            System.setOut(out);
            System.out.println(actual);
            System.out.println(actual2);

            String expected = "a@bc.com,jim,ff,buyer,,x";
            String expected2 = "";

            assertEquals(expected, actual);
            assertEquals(expected2, actual2);

            reader.close();
            reader2.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void parseItem() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new PrintWriter("FMItems.csv"));

        ArrayList<String> expectedList = new ArrayList<>();
        ArrayList<String> actualList = new ArrayList<>();

        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);

        writer.println("Jim's Chairs,awesome chair,the best chair your rear has ever neared!,5,39.99");
        writer.close();

        expectedList.add("Jim's Chairs,awesome chair,the best chair your rear has ever neared!,5,39.99");
        actualList = input.parseItem();

        assertEquals(expectedList.get(0), actualList.get(0));
    }

    @Test
    void parseStore() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new PrintWriter("FMStores.csv"));

        ArrayList<String> expectedList = new ArrayList<>();
        ArrayList<String> actualList = new ArrayList<>();

        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);

        writer.println("Cedar Tables,a@bc.com");
        writer.close();

        expectedList.add("Cedar Tables,a@bc.com");
        actualList = input.parseStore();

        assertEquals(expectedList.get(0), actualList.get(0));
    }
}
