package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class DB implements inventoryControl {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

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
    public void Ship_order( int orderNumber )throws OrderException{
        this.setConnections();
        if(connection!=null) {
            try {
                HashMap<Integer, Integer> OrderProductQuantity = new HashMap<Integer, Integer>();
                HashMap<Integer, Integer> CurrentProductQuantity = new HashMap<Integer, Integer>();
                resultSet = statement.executeQuery("select ProductID, Quantity from orderdetails where OrderID = " + orderNumber + ";");

                while (resultSet.next()) {

                    OrderProductQuantity.put(resultSet.getInt("ProductID"), resultSet.getInt("Quantity"));
                }
                for (int productKey : OrderProductQuantity.keySet()) {

                    resultSet = statement.executeQuery("select UnitsInStock from products where ProductID = " + productKey + ";");
                    resultSet.next();
                    int UnitInStock = resultSet.getInt("UnitsInStock");
                    int result = statement.executeUpdate("update products set UnitsInStock ="+ UnitInStock+"  where ProductID ="+productKey+";");
                    int result2 = statement.executeUpdate("update orders set ShippedDate = current_date()  where OrderID = " + orderNumber + ";");
                }

            } catch (Exception OrderException) {
                System.out.println(OrderException.getMessage());
            }
        }
        this.closeConnection();
    };
    public int Issue_reorders( int year, int month, int day ){
        int supplierNum = 0;
        String Strmonth = "";
        if(month < 10){
            Strmonth= "0"+month;
        }else{
            Strmonth = month+"";
        }

        String Strday = "";
        if(day < 10){
            Strday= "0"+day;
        }else{
            Strday = ""+day;
        }
        String date = year+"-"+Strmonth+"-"+Strday;
        this.setConnections();
        if(connection!=null) {
            try {
                HashMap<Integer, Integer> ProductUnitsInStock = new HashMap<Integer, Integer>();
                HashMap<Integer, Integer> ProductReorderLevel = new HashMap<Integer, Integer>();
                HashMap<Integer, Integer> ProductSupplierID = new HashMap<Integer, Integer>();


                resultSet = statement.executeQuery("select ProductID,UnitsInStock, ReorderLevel,SupplierID from products where ReorderLevel > UnitsInStock and UnitsOnOrder = 0;");
                while (resultSet.next()) {
                    ProductUnitsInStock.put(resultSet.getInt("ProductID"), resultSet.getInt("UnitsInStock"));
                    ProductReorderLevel.put(resultSet.getInt("ProductID"), resultSet.getInt("ReorderLevel"));
                    ProductSupplierID.put(resultSet.getInt("ProductID"), resultSet.getInt("SupplierID"));
                }
                for(int productKey : ProductUnitsInStock.keySet()){
                    int newOrder = ProductReorderLevel.get(productKey)*4;
                    resultSet = statement.executeQuery("select (orderdetails.UnitPrice/1.15) as SupplierPrice from orders left join orderdetails on orders.OrderID = orderdetails.OrderID where orderdetails.ProductID = "+productKey+" and orders.OrderDate <= '"+date+"' order by orders.OrderDate asc;");
                    resultSet.last();
                    float price = resultSet.getFloat("SupplierPrice");
                    int result = statement.executeUpdate("update products set UnitsOnOrder ="+ newOrder+"  where ProductID ="+productKey+";");
                    boolean result2 =statement.execute("INSERT INTO  Purchaseorder(SupplierID, Quantity, Price, ProductID,PurchasedDate)VALUES (" +ProductSupplierID.get(productKey)+ ", " +newOrder+ ", " +price+ ", " +productKey+ ",'" + date+ "');");
                }

                resultSet = statement.executeQuery("select count(distinct(SupplierID)) as SupplierCount from Purchaseorder where PurchasedDate = '"+date+"';");
                resultSet.next();
                supplierNum = resultSet.getInt("SupplierCount");
            }
            catch (Exception OrderException) {
                System.out.println(OrderException.getMessage());
            }
        }
        this.closeConnection();
        return supplierNum;
    };
    public void Receive_order( int internal_order_reference ) throws OrderException{
        this.setConnections();
        if(connection!=null) {
        try {
            resultSet = statement.executeQuery("select ProductID,Quantity from purchaseorder where PurchaseorderID = "+internal_order_reference+";");
            resultSet.next();
            int ProductID  = resultSet.getInt("ProductID");
            int OrderQuantity = resultSet.getInt("Quantity");

            resultSet = statement.executeQuery("select UnitsInStock from products where ProductID = "+ProductID+";");
            resultSet.next();
            int UnitsInStock  = resultSet.getInt("UnitsInStock");
            int newStock = OrderQuantity + UnitsInStock;

            int result1 = statement.executeUpdate("update products set UnitsInStock = "+newStock+"  where ProductID = "+ProductID+"");
            int result2 = statement.executeUpdate("update purchaseorder set ArrivalDate = current_date() where PurchaseorderID = "+internal_order_reference+";");
            System.out.println(result1);
            System.out.println(result2);


        }catch(Exception OrderException){
            System.out.println(OrderException.getMessage());
        }
            }
        this.closeConnection();
    };

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
