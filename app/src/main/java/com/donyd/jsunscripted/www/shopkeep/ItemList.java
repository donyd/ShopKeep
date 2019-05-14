package com.donyd.jsunscripted.www.shopkeep;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.donyd.jsunscripted.www.shopkeep.sqlite.DatabaseHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.donyd.jsunscripted.www.shopkeep.MainActivity.itemCount;
import static com.donyd.jsunscripted.www.shopkeep.MainActivity.runningTotal;
import static com.donyd.jsunscripted.www.shopkeep.MainActivity.toolbarInfo;
import static com.donyd.jsunscripted.www.shopkeep.MainActivity.shoppingList;

// code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
public class ItemList extends AppCompatActivity {

//    public static ArrayList<Item> shoppingList = new ArrayList<Item>();
    private static final String TAG = "setItemDataAdapter";

    // Global reference variables
    EditText ETPrice, ETName;
    Toolbar myToolbar;
    MenuItem cartIcon;
    Button btnSaveShop;

    // Decimal Formatting to two places
    DecimalFormat decimalFormat = new DecimalFormat("##.##");

    // Swipe adaptor setup
    private ItemDataAdapter mAdapter;
    SwipeController swipeController = null;

    // Database helper
    DatabaseHelper mDatabaseHelper;
    boolean shoppingAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Swipe Adapter to display list of items
        // code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
        setItemDataAdapter(shoppingList);
        setupRecyclerView();

        // Reinstate toolbar items and values using intent extra
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // hide cart icon
        invalidateOptionsMenu();

        // Instantiate databasehelper
        mDatabaseHelper = new DatabaseHelper(this);

        // UI References
        btnSaveShop = (Button) findViewById(R.id.btnSaveShop);

        // Button saveShop onClick event handler
        btnSaveShop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (shoppingList != null){
                    // Add shoppinglist items to database
                    // Code adapted from https://www.youtube.com/watch?v=aQAIMY-HzL8
                    shoppingAdded = mDatabaseHelper.addShoppingList(shoppingList);
                } // eof if

                if (shoppingAdded) { // reset list and values if successful
                    shoppingList.clear();
                    toolbarInfo = "0 Items | € 0";
                    myToolbar.setTitle(toolbarInfo);
                    setItemDataAdapter(shoppingList);
                    setupRecyclerView();

                    // Toast message to let user know transaction was successful
                    // https://www.javatpoint.com/android-toast-example
                    Toast.makeText(getApplicationContext(), "Shopping Added!", Toast.LENGTH_SHORT).show();
                    Log.i("DatabaseCHK", "Shopping add successful");
                } else {
                    Log.i("DatabaseCHK", "Shopping add unsuccessful");
                }
            } // eof onClick
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("Lifecycle", "ItemList Started");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("Lifecycle", "ItemList Restarted");
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", "ItemList Resumed");
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", "ItemList Paused");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("Lifecycle", "ItemList Stopped");
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "ItemList Destroyed");
    }

    // Store item details on activity interruption
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // TODO [personal]: add retrieval system to extract details capture for Bundle outState ?
        // TODO [personal]: put data captured in outState
        outState.putInt("ItemCountKey", itemCount);
        outState.putFloat("RunningTotal", runningTotal);
        Log.i("Save", Integer.toString(itemCount));

    }




    // Code adapted from https://medium.com/@101/android-toolbar-for-appcompatactivity-671b1d10f354
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        // Set running total info from PriceGrabber

        myToolbar.setTitle(toolbarInfo);

        // Get reference to the cart icon
        cartIcon = menu.findItem(R.id.action_cart);
        cartIcon.setVisible(false);
        return true;
    }


    // Swipe Adapter
    // code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
    private void setItemDataAdapter(ArrayList<Item> incoming){

        // For Testing purposes
//        ArrayList<Item> items = new ArrayList<>();
//        try {
//            // items = putInArrayList(incomingHashMap);
//            InputStreamReader is = new InputStreamReader(getAssets().open("items.csv"));
//
//            BufferedReader bfReader = new BufferedReader(is);
//            bfReader.readLine();
//            String line;
//            String[] st;
//
//            while((line = bfReader.readLine()) != null) {
//                st = line.split(",");
//                Item item = new Item(st[0], Float.parseFloat(st[1]));
//                //item.setName(st[0]);
//                //item.setPrice(Float.parseFloat(st[1]));
//                items.add(item);
//            }
//        } catch (IOException e){
//            Log.d(TAG, e.toString());
//        }
//        shoppingList = items;

        // End of test stub

        mAdapter = new ItemDataAdapter(incoming);
    }

    private void setupRecyclerView() {
        // Get reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        // Swipe functionality test
        // swipeController = new SwipeController();
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position){
                Log.i(TAG, Integer.toString(mAdapter.getItemCount()) + " " + mAdapter.getItemId(position));
                float currentItemPrice = shoppingList.get(position).getFloatPrice();
                Log.i("price", "Price:" + Float.toString(currentItemPrice) + " Position: " + Integer.toString(position) + " Total : " + Float.toString(runningTotal));

                toolbarInfo = Integer.toString(itemCount) + " Items | \u20ac" + decimalFormat.format(runningTotal);
                myToolbar.setTitle(toolbarInfo);

            }

            @Override
            public void onRightClicked(int position) {
                // Get price of item position of clicked item
                // and calculate new total
                if (itemCount > 0){
                    float currentItemPrice = shoppingList.get(position).getFloatPrice();
                    Log.i("price", "Price:" + Float.toString(currentItemPrice) + " Position: " + Integer.toString(position) + " Total : " + Float.toString(runningTotal));

                    float thisRunningtotal = runningTotal;
                    runningTotal = thisRunningtotal - currentItemPrice;

                    toolbarInfo = Integer.toString(--itemCount) + " Items | \u20ac" + decimalFormat.format(runningTotal);
                } else {
                    toolbarInfo =  "0 Items | \u20ac 0";
                }

                mAdapter.items.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                Log.i(TAG, Integer.toString(mAdapter.getItemCount()) + " " + mAdapter.getItemId(position));

                myToolbar.setTitle(toolbarInfo);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // add itemDecoration to prevent swipe state being lost on scroll
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    } // eof setupRecyclerView

} // eof Class File
