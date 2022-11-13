# CS 18000 GLD Project 4 - Furniture Marketplace

### Description
This is a furniture reselling marketplace where users can buy and sell items. The marketplace listing page shows the store, product name, and price of the available goods. Customers can select a specific product to be taken to that product's page, which will include a description and the quantity available. 

## How to run project
### Step 1 - Run FurnitureMarketplace.java
- This is the main method where users will be able to buy, sell, and view their accounts.
### Step 2 - Account Login & Creation
- Users will have an option to log in to an existing account or create a new account.
> (1) Sign In

> (2) Create an account

> (3) Exit
### Step 3 - Main Dashboard
- If the account type is a Buyer, the Buyer Dashboard is shown with available options and all available items displayed to purchase.
Sample Dashboard

![Buyer Dashboard](https://github.com/ColinWu0403/CS-180000-Project-4/blob/main/images/Buyer%20Dashboard.png "Buyer Dashboard")

- If the account type is a Seller, the Seller dasboard is shown will all available items 
Sample Dashboard
![Seller Dashboard](https://github.com/ColinWu0403/CS-180000-Project-4/blob/main/images/Seller%20Dashboard.png "Seller Dashboard")

### Additional features
#### Files
- Sellers will be able to import and export .csv files containing their product information.
- Buyers will be able to export a .csv file containing their purchase history.
#### Statistics
- Sellers can view a dashboard that lists statistics for each of their stores. Sellers can choose to sort their dashboard by a list of customers with the number of items that they have purchased and a list of products with the number of sales. 
- Customers can view a dashboard with store and seller information. Customers can choose to sort the dashboard by a list of stores by number of products sold and a list of stores by the products purchased by that particular customer.
#### Shopping Cart
- Customers can add products from different stores to a shopping cart to purchase all at once, and can remove any product if they choose to do so.
- Sellers can view the number of products currently in customer shopping carts, along with the store and details associated with the products. 

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
> Creates an account that is appended to FMCredentials.csv and returns either a Buyer or Seller object
> @param scanner : takes scanner input

public static Object signInAccount(Scanner scanner)
> Checks if the user has an account with the email and password and returns that Buyer or Seller object
> @param scanner : takes scanner input

public static String checkExistingCredentials(String email, String usernameOrPassword, String purpose)
> Returns a string to tell whether the user's inputted username or email already exists
> @param String email : string with the user inputted email
> @param String username : string with the user inputted username
> @param String purpose : contains a string with "newAccount" or "signIn"

public static String validItemName(Scanner scanner)
> Check if item already exists in FMItems.csv; returns the inputted string name
> @param scanner : takes scanner input

public static String validUserResponse(Scanner scanner, String[] inputOptions)
> Returns the response from the user if it is a valid response.
> @param String[] inputOptions : possible options that can be accepted the by the user
> @param scanner : takes scanner input

public static Item[] createItemList()
> Returns a list of Item objects to be accessed throughout the program.

public static ArrayList<String> createItemListString()
> Returns string ArrayList of Item to be accessed throughout the program.

public static ArrayList<String> buyerDataArray(String userEmail, String cartOrHist)
> Returns user data from FMCredentials to be used in the Buyer constructor
> @param userEmail : User email to make sure you get the right user data
> @param cartOrHist If Cart : Cart list is returned. If Hist, Purchase History list is returned

public static void printBuyerDashboard(Item[] itemList)
> Prints Dashboard for buyer to view after logging in
> @param itemList : The Item list obtained from the createItemList method.

public static String validStoreName(Scanner scanner)
> Returns the store name from the user if it is a valid store name.
> @param scanner : takes scanner input

public static String buyerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Buyer currentUser)
> Returns different strings to navigate users on the dashboard. 
> @param userChoiceFromDashboard : inputted string from main
> @param currentUser : Buyer object

public static String sellerDashboardNavigation(Scanner scanner, String userChoiceFromDashboard, Seller currentUser)
> Returns different strings to navigate users on the dashboard. 
> @param userChoiceFromDashboard : inputted string from main
> @param currentUser : Seller object
  
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
