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
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
public class ItemList extends AppCompatActivity {

    private static HashMap<String, Float> shoppingList = new HashMap<String, Float>();
    private static final String TAG = "setItemDataAdapter";

    // Global references to own UI elements
    EditText ETPrice, ETName;
    Toolbar myToolbar;
    MenuItem cartIcon;

    // OTHER VARIABLES
    // Shopping Details
    int itemCount;
    float runningTotal;
    String totalInfo;


    // Swipe adaptor setup
    private ItemDataAdapter mAdapter;
    SwipeController swipeController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Retrieve intent extras from PriceGrabber
        // code adapted from https://stackoverflow.com/questions/11452859/android-hashmap-in-bundle
        totalInfo = getIntent().getStringExtra("totalInfo");
        //Log.i("IntentExtra", totalInfo);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            shoppingList = (HashMap) bundle.getSerializable("shoppingListHashMap");
        }

        // Swipe Adapter to display list of items
        // code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
        setItemDataAdapter(shoppingList);
        setupRecyclerView();

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


    // Swipe Adapter
    // code adapted from https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
    private void setItemDataAdapter(HashMap<String, Float> incomingHashMap){
        List<Item> items = new ArrayList<>();

        items = putInArrayList(incomingHashMap);

        mAdapter = new ItemDataAdapter(items);
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
            public void onRightClicked(int position) {
                mAdapter.items.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
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

    }

    // Personal method to transfer HashMap contents
    // to ArrayList with generics
    private static ArrayList<Item> putInArrayList(HashMap<String, Float> input){
        ArrayList<Item> itemsList = new ArrayList<>();

        for (String i : input.keySet()){
            Item item = new Item();
            item.setName(i);
            item.setPrice(input.get(i));
            itemsList.add(item);
        }

        return itemsList;
    }



}
