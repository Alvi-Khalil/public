/*
 * Created by Alvi Khalil on 10/24/18 3:24 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/24/18 3:24 PM
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

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.ClubsRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.EcommerceListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.SoSListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ClubItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.EcommerceLinks;
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

import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.CLUB_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SOS_API;


public class ClubFragment extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = ClubFragment.class.getSimpleName();

    private static final String TAG = "check";

    ImageView drawerOpener;
    String myResponse,message;
    Activity activity;
    ProgressDialog pdClub;
    RecyclerView recyclerView;


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
        View view = inflater.inflate(R.layout.fragment_club, container, false);
        drawerOpener=view.findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();
            }
        });

        recyclerView = view.findViewById(R.id.club_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));





   /*     //static start

        List<ClubItems> arrayListOfClubItems = new ArrayList<>();
        String [] headers = new String[]{"Omuk Club","Tomuk Club", "Shamuk Club"};
        Integer [] Images = new Integer[]{R.drawable.club1,R.drawable.club2,R.drawable.club3};
        String [] links = new String[]{"https://www.daraz.com.bd/catalog/?q=suzuki&_keyori=ss&from=input&spm=a2a0e.home.search.go.73521b94tVpPwl",
                "https://bikroy.com/en/ads?query=Suzuki%20bike",
                "https://www.bagdoom.com/catalogsearch/result/?q=suzuki",
                "https://www.daraz.com.bd/catalog/?q=suzuki&_keyori=ss&from=input&spm=a2a0e.home.search.go.73521b94tVpPwl"};



        pdClub.dismiss();
        for(int i=0;i<Images.length;i++){
            ClubItems clubItems= new ClubItems();
            clubItems.setTestImage(Images[i]);
            clubItems.setUrl(links[i]);
            clubItems.setImage(links[i]);
            clubItems.setHeader(headers[i]);

            arrayListOfClubItems.add(clubItems);
        }


        ClubsRecyclerAdapter customAdapter = new ClubsRecyclerAdapter(activity, arrayListOfClubItems);
        recyclerView.setAdapter(customAdapter);
        //static end
*/



        return view;
    }

    private void loadClubs() {
        if(pdClub==null){
            pdClub = new ProgressDialog(activity);
            pdClub.setMessage("Loading .. ");
            pdClub.setCancelable(false);
            pdClub.setCanceledOnTouchOutside(false);
            pdClub.show();
        }
        getUrlInstance(CLUB_API,new CustomCallBack(pdClub,activity) {
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
                    pdClub.dismiss();
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray ClubArray = null;
                    final List<ClubItems> arrayListOfClub = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        ClubArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < ClubArray.length(); i++) {

                            JSONObject ClubArrayObject = null;

                            ClubArrayObject = ClubArray.getJSONObject(i);
                            ClubItems clubItems= new ClubItems();
                            clubItems.setUrl(ClubArrayObject.getString("club_url"));
                            clubItems.setImage(ClubArrayObject.getString("image"));
                            clubItems.setHeader(ClubArrayObject.getString("name"));
                            clubItems.setBody(ClubArrayObject.getString("description"));






                            arrayListOfClub.add(clubItems);


                        }


                    } catch (JSONException e) {
                        pdClub.dismiss();
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdClub.dismiss();
                            ClubsRecyclerAdapter customAdapter = new ClubsRecyclerAdapter(activity, arrayListOfClub);
                            recyclerView.setAdapter(customAdapter);
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pdClub.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClubs();
    }
}
