/*
 * Created by Alvi Khalil on 11/1/18 9:52 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/24/18 4:13 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MyCatalogAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeCard;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomeViewpager;

import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE_CATALOG_WITH_TYPE_API;


public class BikeCatalog extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = BikeCatalog.class.getSimpleName();

    private static final String TAG = "checkaa";
    String myResponse;
    ProgressBar progressBarSports,CruiserprogressBar,CommuterprogressBar,ScooterprogressBar;
    CustomeViewpager SportsmPager,CruisermPager,CommutermPager,ScootermPager;

    Activity activity;

    ImageView drawerOpener;


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
        View view = inflater.inflate(R.layout.fragment_catalogs_bike, container, false);


        FragmentManager fragmentManager = getChildFragmentManager();
        ((MainActivity) activity).hideKeyboard();

        SportsmPager =  view.findViewById(R.id.kk_pager);
        CruisermPager =  view.findViewById(R.id.kk_pager2);
        CommutermPager =  view.findViewById(R.id.kk_pager3);
        ScootermPager =  view.findViewById(R.id.kk_pager4);

        progressBarSports = view.findViewById(R.id.sports_bike_progress);
        CruiserprogressBar = view.findViewById(R.id.cruiser_bike_progress);
        CommuterprogressBar = view.findViewById(R.id.commuter_bike_progress);
        ScooterprogressBar = view.findViewById(R.id.scooter_bike_progress);


       /* progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait ..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/

        return view;
    }

    private void loadBikes() {
        getUrlInstance(BIKE_CATALOG_WITH_TYPE_API, new CustomCallBack(progressBarSports,activity) {
            @Override
            public void sendResponse(Response response) {

                try {
                    myResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {



                    JSONObject fileObject = null;
                    JSONObject dataObject = null;
                    JSONArray SportsArray = null;
                    JSONArray CruiserArray = null;
                    JSONArray CommuterArray = null;
                    JSONArray ScooterArray = null;
                    final List<BikeCard> arrayListOfSports = new ArrayList<>();
                    final List<BikeCard> arrayListOfCruiser = new ArrayList<>();
                    final List<BikeCard> arrayListOfCommuter = new ArrayList<>();
                    final List<BikeCard> arrayListOfScooter = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        dataObject = new JSONObject(fileObject.getString("data"));
                        SportsArray = dataObject.getJSONArray("Sports");
                        CruiserArray = dataObject.getJSONArray("Cruiser");
                        CommuterArray = dataObject.getJSONArray("Commuter");
                        ScooterArray = dataObject.getJSONArray("Scooter");




                        for (int i = 0; i < SportsArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = SportsArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));
                            singleBikeCard.setID(BikeArrayObject.getString("id"));


                            arrayListOfSports.add(singleBikeCard);

                        }

                        for (int i = 0; i < CruiserArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = CruiserArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));
                            singleBikeCard.setID(BikeArrayObject.getString("id"));

                            arrayListOfCruiser.add(singleBikeCard);

                        }

                        for (int i = 0; i < CommuterArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = CommuterArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));
                            singleBikeCard.setID(BikeArrayObject.getString("id"));


                            arrayListOfCommuter.add(singleBikeCard);

                        }

                        for (int i = 0; i < ScooterArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = ScooterArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));
                            singleBikeCard.setID(BikeArrayObject.getString("id"));


                            arrayListOfScooter.add(singleBikeCard);

                        }





                    } catch (JSONException e) {
                        backgroundThreadShortToast(activity,"Something went wrong fetching bike catalog!");
                        /*progressBarSports.setVisibility(View.GONE);
                        CruiserprogressBar.setVisibility(View.GONE);
                        CommuterprogressBar.setVisibility(View.GONE);
                        ScooterprogressBar.setVisibility(View.GONE);*/
                        //progressDialog.dismiss();
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBarSports.setVisibility(View.GONE);
                            CruiserprogressBar.setVisibility(View.GONE);
                            CommuterprogressBar.setVisibility(View.GONE);
                            ScooterprogressBar.setVisibility(View.GONE);

                            MyCatalogAdapter myCatalogAdapter = new MyCatalogAdapter(activity,arrayListOfSports);
                            SportsmPager.setAdapter(myCatalogAdapter);
                            SportsmPager.setAnimationEnabled(true);
                            SportsmPager.setFadeEnabled(true);
                            SportsmPager.setFadeFactor(0.5f);

                            MyCatalogAdapter myCatalogAdapter2 = new MyCatalogAdapter(activity,arrayListOfCruiser);
                            CruisermPager.setAdapter(myCatalogAdapter2);
                            CruisermPager.setAnimationEnabled(true);
                            CruisermPager.setFadeEnabled(true);
                            CruisermPager.setFadeFactor(0.5f);

                            MyCatalogAdapter myCatalogAdapter3 = new MyCatalogAdapter(activity,arrayListOfCommuter);
                            CommutermPager.setAdapter(myCatalogAdapter3);
                            CommutermPager.setAnimationEnabled(true);
                            CommutermPager.setFadeEnabled(true);
                            CommutermPager.setFadeFactor(0.5f);

                            MyCatalogAdapter myCatalogAdapter4 = new MyCatalogAdapter(activity,arrayListOfScooter);
                            ScootermPager.setAdapter(myCatalogAdapter4);
                            ScootermPager.setAnimationEnabled(true);
                            ScootermPager.setFadeEnabled(true);
                            ScootermPager.setFadeFactor(0.5f);


                           /* new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //progressDialog.dismiss();

                                }

                            }, 0);*/



                        }
                    });


                } else {
                    progressBarSports.setVisibility(View.GONE);
                    CruiserprogressBar.setVisibility(View.GONE);
                    CommuterprogressBar.setVisibility(View.GONE);
                    ScooterprogressBar.setVisibility(View.GONE);
                    //progressDialog.dismiss();
                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBikes();
    }
}
