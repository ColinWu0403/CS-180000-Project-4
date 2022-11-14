import java.io.IOException;

/**
 * User interface with shared methods for Seller.java and Buyer.java
 *
 * @author Benjamin Herrington
 * **/
public interface User {

    /**
     * Gets user email
     * **/
    String getEmail();


    /**
     * Gets name
     * **/
    String getName();

    /**
     * Sets name
     * **/
    void setName(String name);

    /**
     * Get password
     * **/
    String getPassword();

    /**
     * Sets password
     * **/
    void setPassword(String password);


    /**
     * Deletes account
     * **/
    void deleteAccount() throws IOException;
}
