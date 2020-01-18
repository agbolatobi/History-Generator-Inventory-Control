package com.company;

public class Product {
    private int reorderLevel;
    private int unitsInStock;
    private int unitsOnOrder;
    private int MaxOrderSize;
    private int supplierID;

    Product(int Reorder_Level, int Units_In_Stock,int Units_On_Order,int max_order_Size, int SupplierID){
        reorderLevel = Reorder_Level;
        unitsInStock = Units_In_Stock;
        unitsOnOrder = Units_On_Order;
        MaxOrderSize = max_order_Size;
        supplierID =  SupplierID;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public int getUnitsOnOrder() {
        return unitsOnOrder;
    }

    public void setUnitsOnOrder(int unitsOnOrder) {
        this.unitsOnOrder = unitsOnOrder;
    }

    public int getMaxOrderSize() {
        return MaxOrderSize;
    }

    public void setMaxOrderSize(int maxOrderSize) {
        this.MaxOrderSize = maxOrderSize;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }
}
