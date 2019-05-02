package com.donyd.jsunscripted.www.shopkeep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.DecimalFormat;

public class ItemList extends AppCompatActivity {
    // Global references to own UI elements
    EditText ETPrice, ETName;
    Toolbar myToolbar;
    MenuItem cartIcon;

    // OTHER VARIABLES
    // Shopping Details
    int itemCount;
    float runningTotal;
    String totalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Retrieve intent extra from PriceGrabber
        totalInfo = getIntent().getStringExtra("totalInfo");
        Log.i("IntentExtra", totalInfo);

        // Reinstate toolbar items and values using intent extra
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // hide cart icon
        invalidateOptionsMenu();

    }

    // Code adapted from https://medium.com/@101/android-toolbar-for-appcompatactivity-671b1d10f354
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        // Set running total info from PriceGrabber
        myToolbar.setTitle(totalInfo);

        // Get reference to the cart icon
        cartIcon = menu.findItem(R.id.action_cart);
        cartIcon.setVisible(false);
        return true;
    }



}
