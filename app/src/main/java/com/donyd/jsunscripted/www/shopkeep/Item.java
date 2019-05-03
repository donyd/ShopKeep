package com.donyd.jsunscripted.www.shopkeep;

public class Item {
    private String name;
    private float price;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPrice() {
        return Float.toString(price);
    }

    public void setPrice(float price){
        this.price = price;
    }



}
