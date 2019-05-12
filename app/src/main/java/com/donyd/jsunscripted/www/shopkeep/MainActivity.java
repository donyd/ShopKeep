package com.donyd.jsunscripted.www.shopkeep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity {

    // Static variables for maintaining cross activity values
    public static ArrayList<Item> shoppingList = new ArrayList<>();
    // Shopping Details
    public static int itemCount;
    public static float runningTotal;
    public static String toolbarInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPriceGrabber = (Button) findViewById(R.id.btnPriceGrabber);

        btnPriceGrabber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PriceGrabber.class);
                startActivity(intent);
            }
        });
    }
}
