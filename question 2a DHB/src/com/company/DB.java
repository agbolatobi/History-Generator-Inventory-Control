package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

public class DB {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private LinkedHashMap<Integer,HashMap<String,DayRecord>> HistoryGenMap = new LinkedHashMap<Integer,HashMap<String,DayRecord>>();
    private HashMap<Integer,Product> StoreProduct = new HashMap<Integer,Product>();


    Properties credentials = new Properties();
    DBcredentials cred = new DBcredentials();

    private String user = "";
    private String password = "";
    private String database = "";

    DB(){
        cred.setIdentity(credentials);
        user = credentials.getProperty("user");
        password = credentials.getProperty("password");
        database = credentials.getProperty("database");
        this.setConnections();
        if(connection!=null){
            generateHistory();
        }

        this.closeConnection();
    }
    //this method makes and sets the database connections
    private void setConnections(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", user, password);
            statement = connection.createStatement();
            statement.executeQuery("use "+database+";");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //This function gets the Products Information then returns the result as ResultSet
    private ResultSet getProduct(){
        try {
            resultSet = statement.executeQuery("select * from products;");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }
    //This function gets the Products Information then returns the result as ResultSet
    private boolean InsertIntoPurchaseHistory(String OrderDate, int SupplierID, float Price,int Quantity,int ProductID){
        boolean result = false;
        try {
            result =statement.execute("INSERT INTO  Purchaseorder(SupplierID, Quantity, Price, ProductID,PurchasedDate)VALUES (" + SupplierID + ", " + Quantity + ", " + Price + ", " + ProductID + ",'" + OrderDate + "');");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    private ResultSet getProductPurchaseHistory(int ProductID){
        try {
            resultSet = statement.executeQuery("select orderdetails.ProductID,orderdetails.UnitPrice,orderdetails.Quantity,(orderdetails.UnitPrice/1.15) as SupplierPrice,orders.OrderDate from orderdetails right join orders on orderdetails.OrderID = orders.OrderID  where ProductID = "+ProductID+" order by orders.OrderDate desc;");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }

    private void generateHistory(){

        try{
            ResultSet Product = getProduct();
            HashMap<Integer,Product> ProductList = new HashMap<Integer, Product>();
                while(Product.next()){
                    int reorderLevel = 0;
                    int orderSize = 0;
                    if(Product.getInt("ReorderLevel")==0){
                        if(Product.getInt("UnitsInStock") == 0){
                            reorderLevel = Product.getInt("UnitsInStock")/4;
                        }else{
                            reorderLevel = 20;
                        }
                    }else{
                        reorderLevel = Product.getInt("ReorderLevel");
                    }

                    if(Product.getInt("UnitsOnOrder") == 0){
                        orderSize = reorderLevel * 4;
                    }else{
                        orderSize = Product.getInt("UnitsOnOrder");
                    }
                    Product newProduct = new Product(reorderLevel,Product.getInt("UnitsInStock"),Product.getInt("UnitsOnOrder"),orderSize,Product.getInt("SupplierID"));
                    StoreProduct.put(Product.getInt("ProductID"),newProduct);
                    ProductList.put(Product.getInt("ProductID"),newProduct);
                }

                for(int productID : ProductList.keySet()){
                    ResultSet ProductHistory = getProductPurchaseHistory(productID);
                    LinkedHashMap<String,DayRecord> ProductDayItems = new LinkedHashMap<String,DayRecord>();
                    HistoryGenMap.put(productID,ProductDayItems);
                    while(ProductHistory.next()){
                        if(HistoryGenMap.get(productID).containsKey(""+ProductHistory.getDate("OrderDate"))){
                            HistoryGenMap.get(productID).get(""+ProductHistory.getDate("OrderDate")).setDay_sales(HistoryGenMap.get(productID).get(""+ProductHistory.getDate("OrderDate")).getDay_sales()+ProductHistory.getInt("Quantity"));
                        }else{
                            DayRecord newDayRecord = new DayRecord(0,0,ProductHistory.getInt("Quantity"),0,0);
                            HistoryGenMap.get(productID).put(""+ProductHistory.getDate("OrderDate"),newDayRecord);
                        }
                        HistoryGenMap.get(productID).get(""+ProductHistory.getDate("OrderDate")).setPrice(ProductHistory.getFloat("SupplierPrice"));
//                        System.out.println(
//                                ProductHistory.getInt("ProductID")+" "+
//                                ProductHistory.getFloat("UnitPrice")+" "+
//                                ProductHistory.getInt("Quantity")+" "+
//                                ProductHistory.getFloat("SupplierPrice")+" "+
//                                ProductHistory.getDate("OrderDate"));
                    }
                    RebuildHistory();
                    printGenTable();
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void RebuildHistory(){
            for(int productID : HistoryGenMap.keySet()){
                HashMap<String,DayRecord> productInformation = HistoryGenMap.get(productID);
                int current_inventory = StoreProduct.get(productID).getUnitsInStock();
                int reorder_level = StoreProduct.get(productID).getReorderLevel();
                int maxOrder = StoreProduct.get(productID).getMaxOrderSize();
                    for(String productDate : productInformation.keySet()){
                        DayRecord day = productInformation.get(productDate);
                        int day_sales = day.getDay_sales();
                        day.setDay_end_inventory(current_inventory);
                        if(current_inventory>=maxOrder){
                            day.setReordered_units(current_inventory-reorder_level);
                            day.setSales_day_end_inventory(reorder_level);
                            day.setDay_start_inventory(reorder_level+day_sales);
                        }else{
                            day.setSales_day_end_inventory(day.getDay_end_inventory());
                            day.setDay_start_inventory(day_sales+day.getSales_day_end_inventory());
                        }
                        current_inventory=day.getDay_start_inventory();
                    }
            }
        }

        public void printGenTable(){

            for(int productID : HistoryGenMap.keySet()){
                for(String DateKey : HistoryGenMap.get(productID).keySet()){
                    DayRecord productDayInformation = HistoryGenMap.get(productID).get(DateKey);
                    if(productDayInformation.getReordered_units()!=0){
                        InsertIntoPurchaseHistory(DateKey,StoreProduct.get(productID).getSupplierID(),productDayInformation.getPrice(),productDayInformation.getReordered_units(),productID);
                    }
                }

            }
        }


    //this function classes the database connections and the statement connections
    private void closeConnection(){
        try {
            statement.close();
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
