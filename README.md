# CS 18000 GLD Project 4 - Furniture Marketplace

### Description
This is a furniture reselling marketplace where users can buy and sell items. Users can create an account that is either a Buyer or Seller with their information saved between sessions. To access their account after creation, users just have to log into their account by inputting the correct credentials. 

For buyers, the Buyer Dashboard shows a marketplace listing page shows the store, product name, and price of the available goods. Buyers also have many options like adding items to shopping cart, purchasing items, and editing account information. More information is below.

For sellers, the Seller Dashboard show different options including creating and managing stores, viewing customer and sale information, and other functions. More information is below.

# How to run project
## Step 1 - Run FurnitureMarketplace.java
- This is the main method where users will be able to buy, sell, and view their accounts.
## Step 2 - Account Login & Creation
- Users will have an option to log in to an existing account or create a new account.
- (1) Sign In
- (2) Create an account
- (3) Exit

## Step 3 - Main Dashboard
- If the account type is a Buyer, the Buyer Dashboard is shown with available options and all available items displayed to purchase.
Sample Dashboard

![Buyer Dashboard](https://github.com/ColinWu0403/CS-180000-Project-4/blob/main/images/Buyer%20Dashboard.png "Buyer Dashboard")

- If the account type is a Seller, the Seller dasboard is shown will all available items 
Sample Dashboard
![Seller Dashboard](https://github.com/ColinWu0403/CS-180000-Project-4/blob/main/images/Seller%20Dashboard.png "Seller Dashboard")

## Buyer features
### (1) Select Product
- Selects product and displays the product information. Users will have an option to add the item and quantity to their shopping cart.

### (2) View Cart
- Views all items currently in shopping cart. Has options to checkout all items, remove specific item from shopping cart, and return back to dashboard.

### (3) Search
- Searches and filters dashboard by Product name, Store, and Description. The dashboards can be sorted by Price and Quantity.

### (4) Review Purchase History
- Users can view their purchase history and export a file with their purchase history

### (5) Manage Account
- Users can change their username, password, or delete their account

### (6) View Statistics
- Users can view the statistics of all stores by products sold or all stores that the user has purchased from. These statistics can be sorted by amount.

### (7) Sign Out
- Takes user back to sign in page

## Seller features
### (1) Manage Stores
- Manages all stores owned by seller. Sellers can edit their exisiting stores, create stores, and delete stores.

### (2) Sales List
- Shows a list of all sales by stores owned by user.

### (3) Statistics Dashboard
- Shows a list of customers with the number of items that they have purchased and a list of products with the number of sales. Sellers can sort this list.

### (4) View Current Carts
- Sellers can view all items currently in the carts of buyers. 

### (5) Manage Account
- Sellers can change their username, password, or delete their account.

### (6) Sign out
- Takes user back to sign in page

# Project Members & Roles Contributed
### Colin Wu
- Buyers class
- FurnitureMarketplace class : Buyers options -> (1) Select Product, (2) View Cart (Only printing), (4) Review Purchase History, and (6) View Statistics
- FurnitureMarketplace class : Seller options -> (4) View Current Carts
- Statistics methods in Buyers
- Showing purchase history in Buyers
### Andrei Deaconescu
- Sellers class
- ItemTest class
- StoreTest class
- SellerTest class 
### Dakota Baldwin
- Store class
- Item class
- FurnitureMarketplace class : Seller options -> (3) Statistics Dashboard, (5) Manage Account
- Statistics methods in Sellers
### Benjamin Herrington
- User Class
- FurnitureMarketplace class : Buyer options -> (2) View Cart
- CSV file formatting guide
- Buyer and Seller flowcharts
### Nathan Schneider
- FurnitureMarketplace class : Main Buyer dashboard and Seller dashboard, Seller options -> (1) Manage Stores, (2) Sales List, (5) Manage Account
- Adding products in Buyers
- Purchasing products in Buyers
- Main method for the marketplace interface in FurnitureMarketplace
- Oversaw most pull requests to GitHub

# Class Information
## FurnitureMarketplace.java (Main marketplace)
- Main marketplace where buyers and sellers can buy items, list items to sell, and access important information for their accounts.
- Contains dashboards for Buyer and Seller account types

### Important methods
public static Object createAccount(Scanner scanner)
- Creates an account that is appended to FMCredentials.csv and returns either a Buyer or Seller object
- @param scanner : takes scanner input

public static Object signInAccount(Scanner scanner)
- Checks if the user has an account with the email and password and returns that Buyer or Seller object
- @param scanner : takes scanner input

public static String checkExistingCredentials(String email, String usernameOrPassword, String purpose)
- Returns a string to tell whether the user's inputted username or email already exists
- @param String email : string with the user inputted email
- @param String username : string with the user inputted username
- @param String purpose : contains a string with "newAccount" or "signIn"

public static String validItemName(Scanner scanner)
- Check if item already exists in FMItems.csv; returns the inputted string name
- @param scanner : takes scanner input

public static String validUserResponse(Scanner scanner, String[] inputOptions)
- Returns the response from the user if it is a valid response.
- @param String[] inputOptions : possible options that can be accepted the by the user
- @param scanner : takes scanner input

public static Item[] createItemList()
- Returns a list of Item objects to be accessed throughout the program.

public static ArrayList<String> createItemListString()
- Returns string ArrayList of Item to be accessed throughout the program.

public static ArrayList<String> buyerDataArray(String userEmail, String cartOrHist)
- Returns user data from FMCredentials to be used in the Buyer constructor
- @param userEmail : User email to make sure you get the right user data
- @param cartOrHist If Cart : Cart list is returned. If Hist, Purchase History list is returned

public static void printBuyerDashboard(Item[] itemList)
- Prints Dashboard for buyer to view after logging in
> @param itemList : The Item list obtained from the createItemList method.

public static String validStoreName(Scanner scanner)
- Returns the store name from the user if it is a valid store name.
- @param scanner : takes scanner input

public static String buyerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Buyer currentUser)
- Returns different strings to navigate users on the dashboard. 
- @param userChoiceFromDashboard : inputted string from main
- @param currentUser : Buyer object

public static String sellerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Seller currentUser)
- Returns different strings to navigate users on the dashboard. 
- @param userChoiceFromDashboard : inputted string from main
- @param currentUser : Seller object
  
## Seller.java
- Contains all methods sellers may use

### Important methods
public Item publishItem(String item, String store) throws IOException
- Returns new item to be created, writes the new item to FMItems.csv
- @param item : Name of item to be written
- @param store : Name of store to be written
  
public void createStore(Store store)
- Adds new store object to Store arraylist, initializes store in FMStores.csv
- @param store : Store object to be created

public void deleteStore(Store currentStore)
- Deletes store object from Store arraylist, removes store in FMStores.csv
- @param store : Store object to be removed

public void exportPublishedItems(String storeName)
- Reads through FMItems.csv, writes all entries with specific store name to a new file
- @param storeName : Name of store to be searched for

public static void viewCustomerShoppingCart()
- Prints customer shopping cart info for Sellers

public void deleteAccount()
- Deletes Seller account

public int importItems(String fileName, Store[] stores)
- Adds imported items to stores and returns number of incorrectly formatted items
- @param fileName : Name of file to be imported
- @param stores : Store[] object array to store to

## Buyer.java
- Contains all methods buyers may use

### Important Methods
public static void exportPurchaseHistory(String email)
- Creates a new file of purchase history
- @param email : Email to search for when exporting

public static ArrayList<String> showPurchaseHistory(String email)
- Returns an ArrayList to be printed as the purchase history
- @param email : Email to search for when adding to array list

public static ArrayList<String> showItemsInCart(String email)
- Returns an ArrayList of all the items in a buyer's cart
- @param email : Email to search for when adding to array list

public ArrayList<String> storesFromBuyerProducts(String email)
- Returns Arraylist of stores by the products purchased by that particular customer
- @param email : Email to search for when adding to array list
  
public ArrayList<String> sortStoresFromBuyerProducts(String email)
- Returns Arraylist of stores by the products purchased by that particular customer sorted by products sold.
- @param email : Email to search for when adding to array list
  
public ArrayList<String> storesFromProductsSold()
- Returns ArrayList of stores by number of products sold
- @param email : Email to search for when adding to array list
  
public ArrayList<String> sortStoresProductsSold()
- Returns sorted ArrayList of stores by number of products sold from most to least
- @param email : Email to search for when adding to array list
  
public void addItem(String itemToAdd, String itemName, int quantityToPurchase)
- Adds item to shopping cart
- @param itemToAdd : Other information of item to add
- @param itemName : Name of item to add
- @param quantityToPurchase : Amount to purchase
 
public String printCart()
- Returns string of the cart
  
public void checkout()
- Removes items from cart, adds them purchase history, and adds purchases to respective Store History
  
public void removeItemFromCart(int userChoice)
- Removes an item from a user's cart.
- @param userChoice : Number that the user selects in the main method.
  
public ArrayList<String> csvTemporaryStorage()
- Reads all data that isn't related to the user from the Credentials file and stores it for easy rewriting; returns arraylist of temporary storage
  
public void deleteAccount()
- Deletes Buyer account
  
public ArrayList<String> parseItem()
- Reads through FMItems.csv and returns a String ArrayList of items
  
public ArrayList<String> parseStore()
- Reads through FMStores.csv and returns a String ArrayList of items
  
## User.java
- User interface with shared methods for Seller.java and Buyer.java
String getEmail()
- Gets user email
  
String getName()
- Gets name of user
  
void setName(String name)
- Sets userName to new name
  
String getPassword()
- Gets user password
  
void setPassword(String password)
- Sets user password to new password

void deleteAccount()
- Deletes user account
## Item.java
- Contains all relevant information and methods for items.

### Important methods
public void deleteItem()
- Deletes item from FMItems.csv
  
public void changeField(String field, String newValue)
- Changes field in of item FMItems.csv
- @param field : Field type to change
- @param newValue : New value to change it to
  
public void printItem()
- Prints store name, name, price, quantity, and description of item
  
## Store.java
- Contains inportant information methods about the store of the items

### Important methods
public void addItem(String itemName, String description, int quantity, double price)
- Writes new item to FMItems.csv
- @param itemName : Name of item to be written 
- @param description : Item description to be written
- @param quantity : Quantity of items to be written
- @param price : Price of item to be written
  
public void printItems()
- Prints items in a store so buyers can see there options and sellers can see what items they have listed
  
public String printItemNames()
- Returns string of items names to be printed in FurnitureMarketplace

public static void saveSale(String buyer, Item item, int amountSold)
- Saves sale information for seller; Writes a copy of the information to FMStats.csv
- @param buyer : Buyer name
- @param item : Item object to be accessed
- @param amountSold : Amount sold to be written

public ArrayList<String> showSales()
- Returns an ArrayList to be printed as the store's sale history

public static ArrayList<String> showStats(String storeName, String type)
- Returns arraylist to be printed as store's statistics
- @param storeName : Name of store to search for
- @param type : Type of statistic to show 
  
public static ArrayList<String> sortShowedStats(String storeName, String type)
- Returns arraylist to be printed as store's statistics sorted by amount from most to least
- @param storeName : Name of store to search for
- @param type : Type of statistic to show 
