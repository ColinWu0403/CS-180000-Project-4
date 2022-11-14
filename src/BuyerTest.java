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
class BuyerTest {                   // some of the methods only work if content is added manually to the file
    private final PrintStream out = System.out;
    private ByteArrayOutputStream outputStream;
    @BeforeEach
    public void setup() {                   // to check methods that print to the console
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

//    @AfterEach
//    void reset() {                  // resets files after testing
//        try {
//            FileWriter itemWriter = new FileWriter("FMItems.csv", false);
//            FileWriter storeWriter = new FileWriter("FMStores.csv", false);
//            itemWriter.write("");
//            storeWriter.write("");
//
//            itemWriter.close();
//            storeWriter.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
            Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
            input.purchaseItem("neat desks,neat desk,a neat desk,4,29.99");
            input.purchaseItem("neat desks,real neat desk,a really neat desk,1,299.99");
            Buyer.exportPurchaseHistory("a@bc.com");

            File file = new File("aPurchaseHistory.csv");
            boolean r = file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> actual = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                actual.add(line);
            }
            String[] expected = new String[] { "neat desks!neat desk!a neat desk!4!29.99",
                    "neat desks!real neat desk!a really neat desk!1!299.99" };

            assertEquals(expected[0], actual.get(0));
            assertEquals(expected[1], actual.get(1));

            boolean res = file.delete();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showPurchaseHistory() {
        ArrayList<String> purchaseHistory = new ArrayList<>();
        purchaseHistory.add("neat desks!neat desk!a neat desk!4!29.99");
        purchaseHistory.add("neat desks!real neat desk!a really neat desk!1!299.99");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", purchaseHistory, null);

        String[] expected = new String[] { "neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99" };
        ArrayList<String> actual = Buyer.showPurchaseHistory("a@bc.com");

        assert actual != null;
        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));

        // works only if item added manually to FMCredentials purchase history
    }

    @Test
    void showItemsInCart() {
        ArrayList<String> cart = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!4!29.99");
        cart.add("neat desks!real neat desk!a really neat desk!1!299.99");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, cart);
        input.addItem("neat desks!neat desk!a neat desk!4!29.99", "neat desk", 1);
        input.addItem("neat desks!real neat desk!a really neat desk!1!299.99",
                "real neat desk", 1);

        String[] expected = new String[] { "neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99" };
        ArrayList<String> actual = Buyer.showItemsInCart("a@bc.com");

        assert actual != null;
        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));

        // works only if item added manually to FMCredentials cart
    }

    @Test
    void storesFromBuyerProducts() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("FMStores.csv"));
        writer.println("neat desks,sample");
        writer.flush();
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        input.addItem("neat desks!neat desk!a neat desk!4!29.99", "neat desk", 1);
        input.addItem("neat desks!real neat desk!a really neat desk!1!299.99",
                "real neat desk", 1);

        ArrayList<String> stores = input.storesFromBuyerProducts("sample");

        String expected = "neat desks";
        String actual = stores.get(0).split(",")[0];

        assertEquals(expected, actual);

        PrintWriter reset = new PrintWriter(new FileWriter("FMStores.csv", false));
//        reset.write("");
        writer.close();
        reset.close();

        // incomplete
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
    void addItem() {

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
        String[] expected = new String[] { "neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99" };
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
    void getCart() throws IOException {
        PrintWriter writer = new PrintWriter("FMCredentials.csv");
        writer.write("neat desks!neat desk!a neat desk!4!29.99");
        writer.write("neat desks!real neat desk!a really neat desk!1!299.99");

        ArrayList<String> cart = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!4!29.99");
        cart.add("neat desks!real neat desk!a really neat desk!1!299.99");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, cart);
        String[] expected = new String[] { "neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99" };
        ArrayList<String> actual = input.getCart();

        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));

        writer.close();
    }

    @Test
    void printCart() {
    }

    @Test
    void checkout() {
    }

    @Test
    void removeItemFromCart() {
    }

    @Test
    void csvTemporaryStorage() {
    }

    @Test
    void getPurchaseHistory() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void parseItem() {
    }

    @Test
    void parseStore() {
    }
}