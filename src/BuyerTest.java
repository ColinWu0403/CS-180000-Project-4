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
            String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                    "neat desks!real neat desk!a really neat desk!1!299.99"};

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

        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99"};
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

        String[] expected = new String[]{"neat desks!neat desk!a neat desk!4!29.99",
                "neat desks!real neat desk!a really neat desk!1!299.99"};
        ArrayList<String> actual = Buyer.showItemsInCart("a@bc.com");

        assert actual != null;
        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));

        // works only if item added manually to FMCredentials cart
    }

    @Test
    void storesFromBuyerProducts() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("FMStores.csv"));
        writer.println("neat desks,4");
        writer.flush();
        ArrayList<String> purchaseHistory = new ArrayList<>();
        purchaseHistory.add("neat desks!neat desk!a neat desk!4!29.99");
        Buyer input = new Buyer("jimmy neutron", "a123456@bc.com", "qwertyuiop", null, null);
        input.addItem("neat desks!neat desk!a neat desk!4!29.99", "neat desk", 1);
        input.addItem("neat desks!real neat desk!a really neat desk!1!299.99",
                "real neat desk", 1);
        input.checkout();
        writer.close();

        ArrayList<String> stores = input.storesFromBuyerProducts("a123456@bc.com");

        String expected = "neat desks,5";
        String actual = stores.get(0).split(",")[0];

        assertEquals(expected, actual);

        PrintWriter reset = new PrintWriter(new FileWriter("FMStores.csv", false));
//        reset.write("");

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
        ArrayList<String> purchaseHistory = new ArrayList<>();
        cart.add("neat desks!neat desk!a neat desk!2!25.00");
        purchaseHistory.add("x");
        Buyer input = new Buyer("jim", "a@bc.com", "ff", purchaseHistory, cart);

        input.setCart(cart);

        String expected = "(1) neat desk from neat desks; Quantity: 2; Total Price: $50.00\n";
        String actual = input.printCart();

        assertEquals(expected, actual);
    }

    @Test
    void checkout() {


    }

    @Test
    void removeItemFromCart() throws FileNotFoundException {
        Buyer input = new Buyer("jim", "a@bc.com", "ff", null, null);
        FileOutputStream fos = new FileOutputStream("FMCredentials.csv", false);
        PrintWriter writer = new PrintWriter(new PrintWriter(fos));

        writer.println("a@bc.com,jim,ff,x,John's Chairs!awesome chair!5!39.99~Brad's Tables!ok table!3!29.99");
        writer.close();

        input.removeItemFromCart(1);

        // incomplete
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
        ArrayList<String> purchaseHistory = new ArrayList<>();
        purchaseHistory.add("John's Chairs!awesome chair!5!39.99");

        Buyer input = new Buyer("jake", "jake@bc.com", "ff", purchaseHistory, null);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("John's Chairs!awesome chair!5!39.99");

        ArrayList<String> actual = input.getPurchaseHistory();

        assertEquals(expected, actual);

        // test fails :(
    }

    @Test
    void deleteAccount() {


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
