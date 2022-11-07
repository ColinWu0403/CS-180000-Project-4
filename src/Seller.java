import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

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
    private ArrayList<Store> stores; // a list of stores the seller owns

    public Seller(String name, String email, String password) throws IOException {
        this.name = name;
        this.email = email;
        this.password = password;

        // write login credentials to FMCredentais.csv
        BufferedWriter writer = new BufferedWriter(new FileWriter("FMCredentials.csv"));
        String credentials = email + ";" + password + ";" + "seller" + "\n";
        writer.write(credentials);
        writer.close();

        stores = new ArrayList<>();
        File f = new File("FMStores.csv");
        // Initialize store objects that this seller has created
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            String line = bfr.readLine();
            while (line != null) {
                String [] splitLine = line.split(",");
                if (splitLine[1].equals(email)) {
                    stores.add(new Store(splitLine[1], splitLine[0]));
                }
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createItem() {
        return null;
    }

    public Item publishItem(String item, String store) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("marketplace.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("marketplace.txt"));

        StringBuilder fileContents = new StringBuilder();

        // appends all the contents of the marketplace file to a StringBuilder object
        String line;
        do {
            line = reader.readLine();
            if (line != null) {
                fileContents.append(line).append("\n");
                // if statement not to append the last line of the file which is blank
            }
        } while (line != null);

        // append the new item line to the file contents
        String toAppend = store + ";" + item; // store name in front of item as per formatting guidelines
        fileContents.append(toAppend);

        // write the new file back to the main marketplace text file
        writer.write(fileContents.toString());

        // create a new Item object and return it
        String name = item.split(",")[0];
        String description = item.split(",")[1];
        int quantity = Integer.parseInt(item.split(",")[2]);
        double price = Double.parseDouble(item.split(",")[3]);

        return new Item(store, name, description, quantity, price);
    }

    public void createStore(Store store) {
        stores.add(store);
    }

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
    public void deleteAccount() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("FMCredentials.csv"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("FMCredentials.csv"));

        ArrayList<String> lines = new ArrayList<>();

        // add all lines in FMCredentials.csv to ArrayList
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        // search and remove login credentials for user to delete
        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).split(",")[0].equals(this.email) &&
                    lines.get(i).split(",")[1].equals(this.password)) {
                lines.remove(i);
                break;
            }
        }

        // rewrite the ArrayList to the FMCredentials.csv file
        for (String s : lines) {
            writer.write(s + "\n");
            writer.flush();
        }

        reader.close();
        writer.close();
    }
}
