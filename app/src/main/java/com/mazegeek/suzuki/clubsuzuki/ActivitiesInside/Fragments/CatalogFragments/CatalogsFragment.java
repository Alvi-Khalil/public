/*
 * Created by Alvi Khalil on 11/1/18 9:52 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/31/18 4:48 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.CatalogViewPagerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.ArrayList;
import java.util.List;


public class CatalogsFragment extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = CatalogsFragment.class.getSimpleName();

    private static final String TAG = "checkaa";
    ImageView drawerOpener;
    Button partsBtn,oilBtn,bikeBtn;
    TextView partsTxt,oilTxt,bikesTxt;
    Activity activity;
    ViewPager viewPager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        Log.i(TAG, FRAGMENT_NAME +" onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, FRAGMENT_NAME +" onCreate");
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, FRAGMENT_NAME +" onCreateView");
        View view = inflater.inflate(R.layout.fragment_catalogs, container, false);
        drawerOpener=view.findViewById(R.id.menu);


        partsBtn=view.findViewById(R.id.parts);
        oilBtn=view.findViewById(R.id.oil);
        bikeBtn=view.findViewById(R.id.bikes);
        oilTxt=view.findViewById(R.id.oil_text);
        partsTxt=view.findViewById(R.id.parts_text);
        bikesTxt=view.findViewById(R.id.bikes_text);

        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();
            }
        });


        oilTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) activity).hideKeyboard();
                oilBtn.setVisibility(View.VISIBLE);
                //partsBtn.setVisibility(View.GONE);  Eta chaneg//
                bikeBtn.setVisibility(View.GONE);
                viewPager.setCurrentItem(2);

            }
        });
        partsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).hideKeyboard();
                //partsBtn.setVisibility(View.VISIBLE); Eta chaneg//
                oilBtn.setVisibility(View.GONE);
                bikeBtn.setVisibility(View.GONE);
                viewPager.setCurrentItem(1);
            }
        });
        bikesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).hideKeyboard();
                bikeBtn.setVisibility(View.VISIBLE);
                //partsBtn.setVisibility(View.GONE); Eta chaneg//
                oilBtn.setVisibility(View.GONE);
                viewPager.setCurrentItem(0);

            }
        });





        List<Fragment> fragments = getFragments();
        FragmentManager fragmentManager = getChildFragmentManager();


        viewPager = (ViewPager)view.findViewById(R.id.pager);
        final CatalogViewPagerAdapter adapter = new CatalogViewPagerAdapter(fragmentManager,fragments);
        viewPager.setAdapter(adapter);

        //viewPager.setOffscreenPageLimit(3); Eta chaneg//
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

               if(position==0){
                   bikeBtn.setVisibility(View.VISIBLE);
                   partsBtn.setVisibility(View.GONE);
                   oilBtn.setVisibility(View.GONE);

               }
              /* else if(position==1){
                   partsBtn.setVisibility(View.VISIBLE);
                   oilBtn.setVisibility(View.GONE);
                   bikeBtn.setVisibility(View.GONE);
                                                                                        Eta chaneg//
               }
               else if(position==2){
                   oilBtn.setVisibility(View.VISIBLE);
                   partsBtn.setVisibility(View.GONE);
                   bikeBtn.setVisibility(View.GONE);

               }*/
               else if(position==1){
                   oilBtn.setVisibility(View.VISIBLE);
                   partsBtn.setVisibility(View.GONE);
                   bikeBtn.setVisibility(View.GONE);

               }

            }
        });




        return view;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        BikeCatalog bikeCatalog = new BikeCatalog();
        //PartsCatalog partsCatalog = new PartsCatalog(); Eta chaneg//
        OilCatalog oilCatalog = new OilCatalog();

        fragmentList.add(bikeCatalog);
        //fragmentList.add(partsCatalog); Eta chaneg//
        fragmentList.add(oilCatalog);

        return fragmentList;
    }


}
