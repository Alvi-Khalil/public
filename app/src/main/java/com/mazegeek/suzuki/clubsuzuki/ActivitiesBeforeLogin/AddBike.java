/*
 * Created by Alvi Khalil on 10/23/18 12:17 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/23/18 12:17 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.R;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.AddingChassis;

public class AddBike extends AppCompatActivity {

    CardView addBike;
    TextView skip;
    EditText editChassis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);
        addBike = findViewById(R.id.card);
        skip = findViewById(R.id.skip);
        editChassis = findViewById(R.id.edit_name);
        addBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editChassis.getText().toString().trim().isEmpty()){
                    if (editChassis.getText().toString().length()>16){

//                        AddingChassis(AddBike.this,editChassis.getText().toString());
//
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(AddBike.this,MainActivity.class);
                                startActivity(intent);

                            }

                        }, 500);


                        //Toast.makeText(activity, "Sent to admin for approval", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddBike.this, "Chassis number length must be 17 or more!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddBike.this, "Empty Chassis Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBike.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
