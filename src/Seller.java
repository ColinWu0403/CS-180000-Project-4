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

//    public void publishItem(Item item, Store store) {
//       // unable to implement until Item and Store classes available
//    }

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
