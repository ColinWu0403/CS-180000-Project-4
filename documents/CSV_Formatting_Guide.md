# CSV Formatting Guide

#### By Benjamin Herrington

*If you need to edit the formatting for ANY REASON, please notify all other team members first because it will affect their code and there is likely another solution.*

### All Credentials are in a single csv, titled "FMCredentials"
The way in which you read the PurchaseListing/PurchaseHistory is dependent on whether or not they are a buyer or a seller
B: (Store,item,quantity,price) S:(Customer sold to,Store,item,quantity,price)
Cart format is identical to Buyer History format
Different Purchases/Sales are seperated by "@" meanwhile the details of those Purchases/Sales are seperated by "!". This will make the list easy to create
Shopping cart Follows similar convention. Different items seperated by "@", details seperated by "!"
LOGIN CREDENTIALS: Email,Username,password,buyer/seller,PurchaseListing/PurchaseHistory, shopping cart if user is a Buyer
> Ex: "JohnSmith@gmail.com","SmithChairs17","123456","buyer",@"John's Chairs"!"awesome chair"!"5"!"39.99"@"John's Couchs"!"Epic Couch"!"1"!"399.99",@"Bob's Tables"!"solid table"!"1"!"79.99"

### All Stores are in a single CSV, titled "FMStores"
STORES: storeName,ownerID
> Ex: "John's Chairs","JohnSmith@gmail.com"

### All items stored in a single CSV, titled "FMItems.csv"
PRODUCT: storeName,itemName,description,quantityAvailable,price
> Ex: "John's Chairs","awesome chair","the best chair your rear has ever neared!","5","39.99"

