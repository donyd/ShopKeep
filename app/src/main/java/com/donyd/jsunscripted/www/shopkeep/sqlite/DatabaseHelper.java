package com.donyd.jsunscripted.www.shopkeep.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.donyd.jsunscripted.www.shopkeep.Item;
import com.donyd.jsunscripted.www.shopkeep.sqlite.ShopKeepContract.Products;
import com.donyd.jsunscripted.www.shopkeep.sqlite.ShopKeepContract.Purchases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

// adapted from https://app.pluralsight.com/library/courses/android-database-application-sqlite-building-your-first/table-of-contents
// and https://developer.android.com/training/data-storage/sqlite.html
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShopKeep.db";

    public ArrayList<Item> shoppingList = new ArrayList<>();

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TABLE_PRODUCTS_CREATE =
            "CREATE TABLE " + Products.TABLE_NAME + " (" +
                    Products._ID + " INTEGER PRIMARY KEY, " +
                    Products.COLUMN_NAME + " TEXT, " +
                    Products.COLUMN_PRICE + " REAL " + ")";

    private static final String TABLE_PURCHASES_CREATE =
            "CREATE TABLE " + Purchases.TABLE_NAME + " (" +
                    Purchases._ID + " INTEGER PRIMARY KEY, " +
                    Purchases.COLUMN_PRODUCT_ID + " INTEGER, " +
                    Purchases.COLUMN_PURCHASE_DATE + " TEXT, " +
                    Purchases.COLUMN_ESTIMATED_DURATION + " TEXT, " +
                    Purchases.COLUMN_ACTUAL_DURATION + " TEXT, " +
                    " FOREIGN KEY(" + Purchases.COLUMN_PRODUCT_ID + ") REFERENCES " +
                    Products.TABLE_NAME + "(" + Products._ID + ") " + ")";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_PRODUCTS_CREATE);
        sqLiteDatabase.execSQL(TABLE_PURCHASES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Products.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Purchases.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    // code adapted from https://www.youtube.com/watch?v=aQAIMY-HzL8
    public boolean addShoppingList(ArrayList<Item> shoppingList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =  new ContentValues();

        // get current list of products if any
        ArrayList<Item> products = getExistingProducts();

        // Iterator, code adapted from https://www.tutorialspoint.com/iterate-through-arraylist-in-java
        Iterator listLooper = shoppingList.iterator();

        while (listLooper.hasNext()){
            // Loop through products
            // cast to a variable in order to prevent probable skipping
            Item item = (Item) listLooper.next();

            for (int i = 0; i < products.size(); i++){

                if(products.get(i).equals(item)){ // if a product already exists update purchases table
                    contentValues.put(Purchases.COLUMN_PRODUCT_ID, getProdIdByName(item.getName()));
                    contentValues.put(Purchases.COLUMN_PURCHASE_DATE, getDateTime());
                    contentValues.put(Purchases.COLUMN_ESTIMATED_DURATION, getDateTime());
                    contentValues.put(Purchases.COLUMN_ACTUAL_DURATION, getDateTime());

                } else { // update product table as well
                    // Product Table
                    contentValues.put(Products.COLUMN_NAME, item.getName());
                    contentValues.put(Products.COLUMN_PRICE, item.getFloatPrice());

                    // Purchases Table
                    contentValues.put(Purchases.COLUMN_PRODUCT_ID, getProdIdByName(item.getName()));
                    contentValues.put(Purchases.COLUMN_PURCHASE_DATE, getDateTime());
                    contentValues.put(Purchases.COLUMN_ESTIMATED_DURATION, getDateTime());
                    contentValues.put(Purchases.COLUMN_ACTUAL_DURATION, getDateTime());

                } // else
            } // for
        } // while

        long result = db.insert(Purchases.TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        } else
        {
            return true;
        }
    }

    /*
    *   HELPER METHODS FOR:
    *    addShoppingList method above
     */

    // Query string to retrieve product _ID by name
    private int getProdIdByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT " + Products._ID + " FROM "
                + Products.TABLE_NAME + " WHERE " + Products.COLUMN_NAME + " = " + name;

        Cursor productId = db.rawQuery(queryString, null);

        // Close cursor resource
        productId.close();
        return productId.getInt(0);
    }


    // code adapted from https://tips.androidhive.info/2013/10/android-insert-datetime-value-in-sqlite-database/
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);

    } // eof getDateTime


    // TODO: Implement logic for shoppingList capture
    // 1. Take first item from the list
    // 2. If item name already exists in product table,
    //    then insert into purchases table with purchases.product_id = product.id
    // 3. Else if item name doesn't exist in product table,
    //    then insert item into product table
    // 4. Insert item into purchases table with purchases.product_id = product.id

    // Method to retrieve all products table records
    // code adapted from https://app.pluralsight.com/player?course=android-database-application-sqlite-building-your-first&author=simone-alessandria&name=android-database-application-sqlite-building-your-first-m3&clip=6&mode=live
    // also previously utilized here https://github.com/donyd/FitnessAppPrototype/blob/master/app/src/main/java/com/unscripted/www/fitnessappprototype/WorkoutActivity.java
    private ArrayList<Item> getExistingProducts(){
        // Get instance of database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query string to retrieve all products
        String selectProductsQuery = "SELECT " + Products._ID + ", " + Products.COLUMN_NAME + " FROM "
                + Products.TABLE_NAME;

        // Retrieve records in a Cursor set object
        Cursor productSet = db.rawQuery(selectProductsQuery, null);

        // Verifying query return
        int i = productSet.getCount();
        Log.d("Record Count", String.valueOf(i));

        // Iterate through returned cursor set and
        // populate items with values
        while(productSet.moveToNext()){
            // retrieve columns _ID and name from each row and add to item
            // adapted from https://developer.android.com/reference/android/database/Cursor
            Item currItem = new Item(productSet.getInt(0), productSet.getString(2));
            // add item to
            shoppingList.add(currItem);
        }

        // Close resources
        productSet.close();
        return shoppingList;

    } // eof readData

} // eof Class File
