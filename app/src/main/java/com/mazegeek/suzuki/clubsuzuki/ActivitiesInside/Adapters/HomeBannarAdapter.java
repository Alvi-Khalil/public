package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.OfferDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.io.Serializable;
import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BANNER;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_PREVIOUS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class HomeBannarAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    private List<BannarImages> s;







    public HomeBannarAdapter(Context context,List<BannarImages> s){
        this.context=context;
        this.s=s;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return s.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view=inflater.inflate(R.layout.slide,container,false);


        ImageView imageView= view.findViewById(R.id.image);
        TextView textViewTitle= view.findViewById(R.id.title);
        TextView textViewSubtitle= view.findViewById(R.id.sub);
        LinearLayout linearLayoutDetails= view.findViewById(R.id.details_lin);
        final BannarImages st = s.get(position);



        gluideLoader(context,st.getBannar(),imageView);

        textViewTitle.setText(st.getBannarTitle());
        textViewSubtitle.setText(st.getBannarSubtitle());

        linearLayoutDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(context,OfferDetails.class);
                intent.putExtra("Extra",  st);
                intent.putExtra(EXTRA_PREVIOUS,  BANNER);
                context.startActivity(intent);
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view.equals(object));
    }


}
