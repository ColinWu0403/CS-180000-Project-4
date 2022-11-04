# CSV Formatting Guide

#### By Benjamin Herrington

*If you need to edit the formatting for ANY REASON, please notify all other team members first because it will effect their code and there is likely another solution.*

### All Credentials are in a single csv, titled "FMCredentials"
LOGIN CREDENTIALS: Email,Username,password,buyer/seller
> Ex: "JohnSmith@gmail.com","SmithChairs17","123456","buyer"

### All Stores are in a single CSV, titled "FMStores"
STORES: storeName,ownerID
> Ex: "John's Chairs","JohnSmith@gmail.com"

### All items stored in a single CSV, titled "FMItems.csv"
PRODUCT: storeName,itemName,description,quantityAvailable,price
> Ex: "John's Chairs","awesome chair","the best chair your rear has ever neared!","5","39.99"

### The name of the CSV below will follow the naming convention of concatenating "PurchaseListing.csv" to the store name. Note that Both Purchase and Listing are capitalized
Follow this convention so the program can open the correct file
> Ex: ChairStorePurchaseListing.csv
*When you need to get the total number of unique items sold to a customer, you will have to count the occurences of that customers name index 1 for every line in the file.*
SELLER STATISTICS: storeName,customerSoldTo,itemName,quantityOfItem,price
> Ex: "John's Chairs","ChairLover@gmail.com,"awesome chair","3","39.99"

### The name of the CSV below will follow the naming convention of concatenating "PurchaseHistory.csv" to the Buyer **EMAIL** and not Username
Buyer Username can and will be changed, so csv file would have complications if it was associated with Username
> Ex: JohnSmith@gmail.comPurchaseHistory.csv
BUYER PURCHASE HISTORY: storeName,itemName,quantityOfItem,price
> Ex: "John's Chairs",awesome chair","3","39.99"

### The name of the CSV below will follow the naming convention of concatenating "Cart.csv" to the Buyer **EMAIL** and not Username
Buyer Username can and will be changed, so csv file would have complications if it was associated with Username
> Ex: JohnSmith@gmail.comCart.csv
SHOPPING CART: storeName,itemName,quantityOfItem,price
> Ex: "John's Chairs",awesome chair","3","39.99"

### ALL Messages will be stored in a single csv, titled "FMMessages"
USER MESSAGE: recipient,sender,message
> Ex: "JohnSmith@gmail.com","ChairLover@gmail.com","Thank you for the awesome Chair!"
