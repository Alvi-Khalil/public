/*
 * Created by Alvi Khalil on 10/24/18 3:23 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/24/18 11:38 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.OffersRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.SoSListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OfferItem;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.CustomCallBack;
import com.mazegeek.suzuki.clubsuzuki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_OFFERS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SOS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;


public class OffersFragment extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = OffersFragment.class.getSimpleName();

    private static final String TAG = "check";

    ImageView drawerOpener;
    RecyclerView recyclerView;
    Activity activity;
    ProgressDialog pdSos;
    String myResponse,message;

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
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        drawerOpener=view.findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();
            }
        });

        recyclerView = view.findViewById(R.id.sos_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));




/*
        //static start

        List<SoSItems> arrayListOfSoS = new ArrayList<>();
        String [] numbers = new String[]{"999","777", "888"};
        String [] headers = new String[]{"FIRE SERVICE","POLICE", "MEDICAL"};
        Integer [] Images = new Integer[]{R.drawable.fire,R.drawable.alamr,R.drawable.health};


        pdSos.dismiss();
        for(int i=0;i<Images.length;i++){
            SoSItems soSItems= new SoSItems();

            soSItems.setTestImage(Images[i]);
            soSItems.setPhoneNumber(numbers[i]);
            soSItems.setHeader(headers[i]);
            soSItems.setImage(numbers[i]);

            //LogPrint("Kiklo",numbers[i]+" "+headers[i]+" "+Images[i]+"\n");

            arrayListOfSoS.add(soSItems);
        }


        SoSListRecyclerAdapter customAdapter = new SoSListRecyclerAdapter(activity, arrayListOfSoS);
        recyclerView.setAdapter(customAdapter);


        //static end
        */





        return view;
    }

    private void loadOffers() {
        if(pdSos==null){
            pdSos = new ProgressDialog(activity);
            pdSos.setMessage("Loading .. ");
            pdSos.setCancelable(false);
            pdSos.setCanceledOnTouchOutside(false);
            pdSos.show();
        }

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        RequestBody formBody = new FormBody.Builder()
                .add("offer_type_id","1" )
                .build();

        getUrlInstance(ALL_OFFERS,formBody,"Authorization",token,new CustomCallBack(pdSos,activity) {
            @Override
            public void sendResponse(Response response) {
                try {
                    myResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    JSONObject jsonObject = new JSONObject(myResponse);
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    pdSos.dismiss();
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray OfferArray = null;
                    final List<OfferItem> arrayListOfOffer = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        OfferArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < OfferArray.length(); i++) {

                            JSONObject OfferArrayObject = null;

                            OfferArrayObject = OfferArray.getJSONObject(i);
                            OfferItem offerItem= new OfferItem();
                            offerItem.setTitle(OfferArrayObject.getString("title"));
                            offerItem.setSubTitle(OfferArrayObject.getString("sub_title"));
                            offerItem.setDetails(OfferArrayObject.getString("description"));
                            offerItem.setDate(OfferArrayObject.getString("created_at"));
                            offerItem.setType(OfferArrayObject.getString("type"));
                            offerItem.setImage(OfferArrayObject.getString("image"));
                            offerItem.setUrl(OfferArrayObject.getString("url"));






                            arrayListOfOffer.add(offerItem);


                        }


                    } catch (JSONException e) {
                        pdSos.dismiss();
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdSos.dismiss();
                            OffersRecyclerAdapter customAdapter = new OffersRecyclerAdapter(activity, arrayListOfOffer);
                            recyclerView.setAdapter(customAdapter);
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pdSos.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOffers();
    }
}
