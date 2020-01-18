package com.company;

public class DayRecord {
    private int day_end_inventory;
    private int sales_day_end_inventory;
    private  int day_start_inventory;
    private int day_sales;
    private int reordered_units;
    private float price;

            DayRecord(int dayEndInventory, int dayStartInventory, int daySales, int reorderedUnits, int salesDayEndInventory){
        this.day_end_inventory = dayEndInventory;
        this.day_start_inventory = dayStartInventory;
        this.day_sales = daySales;
        this.reordered_units = reorderedUnits;
        this.sales_day_end_inventory = salesDayEndInventory;
    }

    public void setDay_end_inventory(int day_end_inventory) {
        this.day_end_inventory = day_end_inventory;
    }

    public void setDay_start_inventory(int day_start_inventory) {
        this.day_start_inventory = day_start_inventory;
    }

    public void setDay_sales(int day_sales) {
        this.day_sales = day_sales;
    }

    public void setReordered_units(int reordered_units) {
        this.reordered_units = reordered_units;
    }

    public int getDay_end_inventory() {
        return day_end_inventory;
    }

    public int getDay_sales() {
        return day_sales;
    }

    public int getDay_start_inventory() {
        return day_start_inventory;
    }

    public int getReordered_units() {
        return reordered_units;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public int getSales_day_end_inventory() {
        return sales_day_end_inventory;
    }

    public void setSales_day_end_inventory(int sales_day_end_inventory) {
        this.sales_day_end_inventory = sales_day_end_inventory;
    }
}
