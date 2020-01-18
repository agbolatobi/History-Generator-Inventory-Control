## HISTORY GENERATOR PROGRAM
**Overview**
The History Generator program rebuilds the history of transactions by taking the current inventory, the reorder level and transactions then generating the history by back tracking through the them. The program creates the products, its properties and assumption variables in the product object and stores the daily information in the day records objects then then permutates though them to generate to the history of those products. 

## File and External Data
**Main.java**:This is the main class where all the objects are called, and the input are accepted and validated.
**DB.java**:This Class makes the database connections, generates the historical transactions and inserts the transactions to the Purchaseorders Tables
**DBcredentials.java**:This class sets the database credential and returns then to any class that needs to access the database.
**DayRecord.java**:This Class Contains the Daily Record of products Transactions. The starting inventory, Ending Inventory, Price Quantity sold and reorder level
**Product.java**:This Class Stores the Products Information and this is used to generate the past transactions from the products records 

## Data structures and their relations to each other’s
- **HistoryGenMap**: This is a Linked Hash Map that contains Another Linked Hash Map the first Linked Hash  Map’s Key is the product Id and the value is a Linked Hash Map of Day Record Objects 
 - **StoreProduct**: This is a Hash Map of Product Objects the Key of this hash map is the Product ID.
## Assumptions
 - Days without Reordered units are included in the History of Transactions 
 - The PurchaseOrders.sql is run and the Purchase Order Table has been created in the database to store the Results of the History.
## Key algorithms and design elements 
The first starting inventory is the current units in stock the sales are added to this inventory to form the end sales inventory and then after comparing the inventory to the maximum order size the if the inventory is above then units are reordered and the reordered until return the inventory to the reorder level when the next sales are added to it. This is how the history is generated and back tracked.
## Limitations
 - The Program needs the Purchase Order table in the database to Store the results of the Program
 - The program assumes the re ordered days will always end on the reorder level so this could affect the accuracy of the generated history.

## Inventory Control
**Overview**
The inventory control program implements the inventory control interface. The DB object connects to the database when it is initialized and when the ship_order, reissue_orders or receive_orders methods are called the  methods starts a connection and runs the queries to carry out the feature.
## File and External Data
**Main.java** This is the main class where all the objects are called, and the input are accepted and validated.
**DB.java** This Class makes the database connections, Implements the InventoryControl interface and is where all the Queries are called
**DBcredentials.java** This class sets the database credential and returns then to any class that needs to access the database.
**OrderException** Extends Exception interface
**InventoryControl** Interface to be implemented 

## Methods and their relations to each other’s
**Ship_order**:this method performs all the queries that it would take to fulfil an order
**Issue_reorder**:this method performs all the queries that it would take to find daily reorders and fulfil them 
**Receive_order**:this method performs all the queries to receive an order. 
## Assumptions
 - The history generation program has been run 
 - The Purchaseorder table has been created
 - Purchaseorder.sql has been run
## Limitations
 - The history generation program must be created before the program can run.
 - The purchase order table must be created.
