package com.company;

public class Main {

    public static void main(String[] args) {
	 DB database = new DB();
	 try{
         database.Ship_order(10250);
         System.out.println(database.Issue_reorders(1998,04,24));
         database.Receive_order(1);
     }catch(Exception OrderException){
	     System.out.println(OrderException.getMessage());
     }

    }
}
