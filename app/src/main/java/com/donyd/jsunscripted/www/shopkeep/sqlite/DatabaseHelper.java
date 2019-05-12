package com.donyd.jsunscripted.www.shopkeep.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        // Iterator, code adapted from https://www.tutorialspoint.com/iterate-through-arraylist-in-java
        Iterator listLooper = shoppingList.iterator();
        while (listLooper.hasNext()){
            // contentValues.put(Purchases.COLUMN_PRODUCT_ID, 1);
            contentValues.put(Purchases.COLUMN_PURCHASE_DATE, getDateTime());
            contentValues.put(Purchases.COLUMN_ESTIMATED_DURATION, getDateTime());
            contentValues.put(Purchases.COLUMN_ACTUAL_DURATION, getDateTime());
        }

        long result = db.insert(Purchases.TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        } else
        {
            return true;
        }
    }

    // code adapted from https://tips.androidhive.info/2013/10/android-insert-datetime-value-in-sqlite-database/
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }

}
