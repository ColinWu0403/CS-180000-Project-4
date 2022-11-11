import java.io.IOException;

public interface User {
    String getEmail();
    //Email cannot be changed.

    String getName();
    void setName(String name);
    //Users may decide to edit their account Username

    String getPassword();
    void setPassword(String password);
    //Users may decide to edit their account password.

    void deleteAccount() throws IOException;
    // Deletes User From Credentials list
    //
    // If Seller, all stores owned by that seller will be deleted from csv file, as well as the items
    // associated with that store. Associated "PurchaseListing.csv" files is deleted
    //
    // If Buyer, Associated PurchaseHistory.csv file is deleted
}
