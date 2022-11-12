import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
/**
 * A Test class for the Item class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2022 -- Project 4</p>
 *
 * @author Andrei Deaconescu
 * @version Nov 12, 2022
 */
class ItemTest {

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
    public void closeReader() {             // resets System to print to terminal
        System.setOut(out);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteItem() throws IOException {
        Item input = new Item("store", "plate", "a plate", 10, 4.99);
        Store store = new Store("a@bc.com", "store");
        store.addItem("table", "a table", 5, 59.99);
        store.addItem("chair", "a chair", 5, 9.99);

        input.deleteItem();

        BufferedReader reader = new BufferedReader(new FileReader("FMItems.csv"));
        ArrayList<Item> actual = store.getItems();

        String line;
        ArrayList<String> lines = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }

        String[] expectedNames = new String[] { "table", "chair" };
        String[] expectedDescriptions = new String[] { "a table", "a chair" };
        int[] expectedQuantities = new int[] { 5, 5 };
        double[] expectedPrices = new double[] { 59.99, 9.99 };

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expectedNames[i], actual.get(i).getName());
            assertEquals(expectedDescriptions[i], actual.get(i).getDescription());
            assertEquals(expectedQuantities[i], actual.get(i).getQuantity());
            assertEquals(expectedPrices[i], actual.get(i).getPrice());
        }
        seller.deleteStore(store);
    }

    @Test
    void getStore() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        String store = input.getStore();
        assertEquals("store", store);
    }

    @Test
    void getName() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        String name = input.getName();
        assertEquals("something nice", name);
    }

    @Test
    void getDescription() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        String description = input.getDescription();
        assertEquals("a real nice item", description);
    }

    @Test
    void getQuantity() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        int quantity = input.getQuantity();
        assertEquals(1, quantity);
    }

    @Test
    void getPrice() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        double price = input.getPrice();
        assertEquals(0.99, price);
    }

    @Test
    void changeField() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);

        input.changeField("name", "something else nice");
        String name = input.getName();
        assertEquals("something else nice", name);

        input.changeField("description", "a different real nice item");
        String description= input.getDescription();
        assertEquals("a different real nice item", description);

        input.changeField("quantity", "2");
        int quantity = input.getQuantity();
        assertEquals(2, quantity);

        input.changeField("price", "9.99");
        double price = input.getPrice();
        assertEquals(9.99, price);
    }

    @Test
    void printItem() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        String expected = "something nice selling at 0.99\n";

        input.printItem();

        assertEquals(expected, outputStream.toString());
    }

    @Test
    void printItemInfo() {
        Item input = new Item("store", "something nice", "a real nice item",
                1, 0.99);
        String expected = "something nice selling at 0.99. 1 in stock.\na real nice item\n";

        input.printItemInfo();

        assertEquals(expected, outputStream.toString());
    }
}