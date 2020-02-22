package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications;
import com.mazegeek.suzuki.clubsuzuki.R;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;

public class NotificationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        ImageView backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView imageView=findViewById(R.id.image);
        TextView textViewTitle=findViewById(R.id.title);
        TextView textViewTime=findViewById(R.id.time);
        TextView textViewDescription=findViewById(R.id.description_text);
        TextView textViewNoDescrip=findViewById(R.id.no_des);
        CardView cardView=findViewById(R.id.descrip_card);


        Notifications notifications=(Notifications) getIntent().getSerializableExtra("Extra");

        if(!notifications.getImage().equals("null")){
            gluideLoader(this,notifications.getImage(),imageView);
        }
        else {
            imageView.setVisibility(View.GONE);
        }

        textViewTitle.setText(notifications.getTitle());

        textViewTime.setText(notifications.getDate());

        if(!notifications.getDescription().equals("null")){
            textViewDescription.setText(notifications.getDescription());
        }
        else{
            cardView.setVisibility(View.GONE);
            textViewNoDescrip.setVisibility(View.VISIBLE);
        }

    }
}
