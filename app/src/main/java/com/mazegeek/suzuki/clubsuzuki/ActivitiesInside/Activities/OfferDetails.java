package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OfferItem;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.io.Serializable;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BANNER;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_PREVIOUS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;

public class OfferDetails extends AppCompatActivity  {

    String urlOfDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        ImageView backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        String s =getIntent().getStringExtra(EXTRA_PREVIOUS);



        ImageView imageView=findViewById(R.id.image);
        TextView textViewTitle=findViewById(R.id.title);
        TextView textViewSub=findViewById(R.id.subtitle);
        TextView textViewTime=findViewById(R.id.time);
        TextView textViewDescription=findViewById(R.id.description_text);
        TextView textViewNoDescrip=findViewById(R.id.no_des);
        TextView textViewURL=findViewById(R.id.url);
        CardView cardView=findViewById(R.id.descrip_card);

        TextView featuredText = findViewById(R.id.offer_type);
        ImageView featuredStar = findViewById(R.id.star);


        if(s.equals(BANNER)){


            BannarImages bannarImages=(BannarImages) getIntent().getSerializableExtra("Extra");


            gluideLoader(this,bannarImages.getBannar(),imageView);

            textViewTitle.setText(bannarImages.getBannarTitle());
            textViewSub.setText(bannarImages.getBannarSubtitle());
            textViewTime.setText(bannarImages.getBannarTime());

            if(!bannarImages.getBannarText().equals("null")){
                textViewDescription.setText(bannarImages.getBannarText());
            }
            else{
                cardView.setVisibility(View.GONE);
                textViewNoDescrip.setVisibility(View.VISIBLE);
            }

            if(!bannarImages.getBannarUrl().equals("null")){
                textViewURL.setText("Click to see details : "+bannarImages.getBannarUrl());
                urlOfDetails=bannarImages.getBannarUrl();
            }
            else{
                textViewURL.setVisibility(View.GONE);
            }
        }
        else{
            OfferItem offerItem=(OfferItem) getIntent().getSerializableExtra("Extra");



            if(!offerItem.getImage().equals("null")){
                gluideLoader(this,offerItem.getImage(),imageView);
            }
            else {
                imageView.setVisibility(View.GONE);
            }


            textViewTitle.setText(offerItem.getTitle());
            textViewSub.setText(offerItem.getSubTitle());
            textViewTime.setText(offerItem.getDate());

            if(!offerItem.getDetails().equals("null")){
                textViewDescription.setText(offerItem.getDetails());
            }
            else{
                cardView.setVisibility(View.GONE);
                textViewNoDescrip.setVisibility(View.VISIBLE);
            }

            if (offerItem.getType().equals("normal")){
                featuredText.setVisibility(View.GONE);
                featuredStar.setVisibility(View.GONE);
            }

            if(offerItem.getUrl()!=null){
                if(!offerItem.getUrl().equals("null")){
                    textViewURL.setText("Click to see details : "+offerItem.getUrl());
                    urlOfDetails=offerItem.getUrl();
                }
                else{
                    textViewURL.setVisibility(View.GONE);
                }
            }
            else{
                textViewURL.setVisibility(View.GONE);
            }



        }

        textViewURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlOfDetails;
                //String url =  "https://wildmentor.org/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }
}
