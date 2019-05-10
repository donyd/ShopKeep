package com.donyd.jsunscripted.www.shopkeep;


import java.io.Serializable;

// code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
public class Item implements Serializable{
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

    public float getFloatPrice() { return price; }

    public void setPrice(float price){
        this.price = price;
    }

    public Item(String name, float price){
        this.name = name;
        this.price = price;
    }

}
