import java.io.*;
import java.nio.Buffer;

/**
 *  The Seller class and all the variables and methods they may use.
 *
 * @author Colin Wu
 * @version 2022-3-11
 */
public class Seller implements User {
    private String name; // the user's name
    private final String email; // the user's email (cannot be changed)
    private String password; // the user's password
//    private ArrayList<Store> stores; // a list of stores the seller owns (Store class not created yet)

    public Seller(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String createItem() {
        return null;
    }

    public void publishItem(String item, String store) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("marketplace.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("marketpalce.txt"));

        StringBuilder fileContents = new StringBuilder();

        // appends all the contents of the marketplace file to a StringBuilder object
        String line;
        do {
            line = reader.readLine();
            if (line != null) {
                fileContents.append(line).append("\n");
                // if statement to not append the last line of the file which is blank
            }
        } while (line != null);

        // append the new item line to the file contents
        String toAppend = store + ";" + item; // store name in front of item as per formatting guidelines
        fileContents.append(toAppend);

        // write the new file back to the main marketplace text file
        writer.write(fileContents.toString());
    }

//    public void createStore(Store store) {
//        stores.add(store); // method not compiling until Store class created
//    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String checkMessage() {
        return null;
    }

    @Override
    public void deleteAccount() {

    }
}
