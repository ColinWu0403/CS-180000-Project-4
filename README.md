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
|         Select Option           |   Furniture Dashboard|
| ------------- |:-------------:| -----:|
| (1) Select Product             ||  (0) Store: Ikea :Product: awesome chair :Price: $39.99 :Quantity: 5 :Description: the best chair your rear has ever neared!|
| (2) View Cart                  ||  (1) Store: Jack's Whiskey Shelves :Product: Whiskey Shelf :Price: $159.99 :Quantity: 40 :Description: Shelf to store whiskey|
| (3) Search                     ||  (2) Store: Cool Tables :Product: Ufo-table :Price: $499.99 :Quantity: 2 :Description: ufo shaped table|
| (4) Review Purchase History    ||  |
| (5) Manage Account             ||  |
| (6) View Statistics            ||  |
| (7) Sign Out                   ||  |
- If the account type is a Seller, the Seller dasboard is shown will all available items 
> (1) Manage Stores
> (2) Sales List
> (3) Statistics Dashboard
> (4) View Current Carts
> (5) Manage Account
> (6) Sign out
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
