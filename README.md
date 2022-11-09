# CS 18000 GLD Project 4 - Furniture Marketplace

### Description
This is a furniture reselling marketplace where users can buy and sell items. The marketplace listing page shows the store, product name, and price of the available goods. Customers can select a specific product to be taken to that product's page, which will include a description and the quantity available. 

## How to run project
### Step 1 - Run FurnitureMarketplace.java
- This is the main method where users will be able to buy, sell, and view their accounts.
### Step 2 - Login/Create new account
- Users will have an option to log in to an existing account or create a new account.
### Step 3 - Buy/Sell products
- Depending on the account type, users will be able to buy products from the marketplace listing, or add products to be sold in the listing.
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
