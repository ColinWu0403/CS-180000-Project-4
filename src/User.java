import java.io.IOException;
import java.util.ArrayList;

public interface User {
    String getEmail();
    //Email cannot be changed.

    String getName();
    void setName(String name);
    //Users may decide to edit their account Username

    String getPassword();
    void setPassword(String password);
    //Users may decide to edit their account password.

    void sendMessage(String message);
    String checkMessage();
    //Both sellers and buyers need to be able to message each other. This will be implemented with csv files
    //See Formatting Documentation for more info

    void deleteAccount() throws IOException;
    // Deletes User From Credentials list
    //
    // If Seller, all stores owned by that seller will be deleted from csv file, as well as the items
    // associated with that store. Associated "PurchaseListing.csv" files is deleted
    //
    // If Buyer, Associated PurchaseHistory.csv file is deleted

    ArrayList<String> parseItem();
    // Reads through FMItems.csv and returns a String ArrayList of items

    ArrayList<String> parseStore();
    // Reads through FMStores.csv and returns a String ArrayList of items
}
