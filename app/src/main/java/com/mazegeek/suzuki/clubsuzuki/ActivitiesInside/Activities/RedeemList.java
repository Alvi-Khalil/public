/*
 * Created by Alvi Khalil on 11/7/18 10:47 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/7/18 10:47 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.mazegeek.suzuki.clubsuzuki.R;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.REDEEM_TYPE;

public class RedeemList extends AppCompatActivity {

    ImageView backBtn;
    CardView cardViewShopping,cardViewServicing,cardViewTour,cardViewAccessories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_list);

        backBtn = findViewById(R.id.back);
        cardViewShopping = findViewById(R.id.shopping);
        cardViewServicing = findViewById(R.id.servicing);
        cardViewTour = findViewById(R.id.tour);
        cardViewAccessories = findViewById(R.id.accessories);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cardViewShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemList.this,ScanQR.class);
                intent.putExtra(REDEEM_TYPE, "Free Shopping");
                startActivity(intent);
            }
        });

        cardViewServicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemList.this,ScanQR.class);
                intent.putExtra(REDEEM_TYPE, "Free Servicing");
                startActivity(intent);
            }
        });

        cardViewTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemList.this,ScanQR.class);
                intent.putExtra(REDEEM_TYPE, "Free Tour");
                startActivity(intent);
            }
        });

        cardViewAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemList.this,ScanQR.class);
                intent.putExtra(REDEEM_TYPE, "Free Accessories");
                startActivity(intent);
            }
        });

    }
}
