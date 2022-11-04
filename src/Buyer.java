import java.util.ArrayList;

/**
 * Buyers class - contains all methods the buyers may use
 *
 * @author Colin Wu
 * @version 2022-3-11
 */
public class Buyer {
    private String name; // Buyer username
    private String email; // Buyer email - This is the unique identifier (Cannot be changed)
    private String password; // Account Password
    private ArrayList<String> purchaseHistory; // Products bought or purchase history
    private ArrayList<String> shoppingCart; // Shopping cart with stuff to buy

    public Buyer (String name, String email, String password) { // Construct Buyers Object
        this.name = name;
        this.email = email;
        this.password = password;
    }

//    public void createShoppingCart(Item shoppingCart) { // initialize shopping cart
//        this.shoppingCart = shoppingCart;
//    }

//    public void addItem(Item item) { // add item to shopping cart
//        shoppingCart.add(item);
//    }

//    public void removeShoppingCart(int itemID) { // remove item from shopping cart
//        shoppingCart.remove(itemID);
//    }

//    public void checkOut() { // Checkout all items from shopping cart / set shopping cart to empty
//        for (int i = 0; i < shoppingCart.size(); i++) {
//            shoppingCart.remove(i);
//        }
//    }

    public ArrayList<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPurchaseHistory(ArrayList<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public void setName(String Name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
