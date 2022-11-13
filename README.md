# CS 18000 GLD Project 4 - Furniture Marketplace

### Description
This is a furniture reselling marketplace where users can buy and sell items. The marketplace listing page shows the store, product name, and price of the available goods. Customers can select a specific product to be taken to that product's page, which will include a description and the quantity available. 

## How to run project
### Step 1 - Run FurnitureMarketplace.java
- This is the main method where users will be able to buy, sell, and view their accounts.
### Step 2 - Account Login & Creation
- Users will have an option to log in to an existing account or create a new account.
- (1) Sign In
- (2) Create an account
- (3) Exit

### Step 3 - Main Dashboard
- If the account type is a Buyer, the Buyer Dashboard is shown with available options and all available items displayed to purchase.
Sample Dashboard

![Buyer Dashboard](https://github.com/ColinWu0403/CS-180000-Project-4/blob/main/images/Buyer%20Dashboard.png "Buyer Dashboard")

- If the account type is a Seller, the Seller dasboard is shown will all available items 
Sample Dashboard
![Seller Dashboard](https://github.com/ColinWu0403/CS-180000-Project-4/blob/main/images/Seller%20Dashboard.png "Seller Dashboard")

### Buyer features
#### (1) Select Product
- Selects product and displays the product information. Users will have an option to add the item and quantity to their shopping cart.

#### (2) View Cart
- Views all items currently in shopping cart. Has options to checkout all items, remove specific item from shopping cart, and return back to dashboard.

#### (3) Search
- Searches and filters dashboard by Product name, Store, and Description. The dashboards can be sorted by Price and Quantity.

#### (4) Review Purchase History
- Users can view their purchase history and export a file with their purchase history

#### (5) Manage Account
- Users can change their username, password, or delete their account

#### (6) View Statistics
- Users can view the statistics of all stores by products sold or all stores that the user has purchased from. These statistics can be sorted by amount.

#### (7) Sign Out
- Takes user back to sign in page

### Seller features
#### (1) Manage Stores
- Manages all stores owned by seller. Sellers can edit their exisiting stores, create stores, and delete stores.

#### (2) Sales List
- Shows a list of all sales by stores owned by user.

#### (3) Statistics Dashboard
- Shows a list of customers with the number of items that they have purchased and a list of products with the number of sales. Sellers can sort this list.

#### (4) View Current Carts
- Sellers can view all items currently in the carts of buyers. 

#### (5) Manage Account
- Sellers can change their username, password, or delete their account.

#### (6) Sign out
- Takes user back to sign in page

## Project Members & Roles Contributed
#### Colin Wu
- Buyers class
- Statistics (Buyers)
- Shopping Cart
#### Andrei Deaconescu
- Sellers class
- Statistics (Sellers)
- InvalidItem class
#### Dakota Baldwin
- Store class
- Item class
#### Benjamin Herrington
- User Class
- FurnitureMarketplace class
- CSV file formatting guide, flowcharts
#### Nathan Schneider
- FurnitureMarketplace class
- Main marketplace interface

## Class Information
### FurnitureMarketplace.java (Main marketplace)
- Main marketplace where buyers and sellers can buy items, list items to sell, and access important information for their accounts.
- Contains dashboards for Buyer and Seller account types

#### Important methods
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
  
### Seller.java
- Contains all methods sellers may use
### Buyer.java
- Contains all methods buyers may use
### User.java
- User interface with shared methods for Seller.java and Buyer.java
### Item.java
- Contains all relevant information for an item and print it when necessary
### Store.java
- Contains pertinent information about the store
