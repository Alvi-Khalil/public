/*
 * Created by Alvi Khalil on 10/24/18 11:31 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/11/18 12:36 PM
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
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.EcommerceListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MapListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.EcommerceLinks;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LocationPoints;
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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ECOMMERCE_API;


public class EcommerceFragment extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = EcommerceFragment.class.getSimpleName();

    private static final String TAG = "check";

    ImageView drawerOpener;
    String myResponse,message;
    Activity activity;
    ProgressDialog pdEcommerce;
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
        View view = inflater.inflate(R.layout.fragment_ecommerce, container, false);
        drawerOpener=view.findViewById(R.id.menu);
        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).drawerOpen();
            }
        });

        recyclerView = view.findViewById(R.id.ecommerce_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));





        //static start
/*

        List<EcommerceLinks> arrayListOfEcommerceLinks = new ArrayList<>();
        String [] links = new String[]{"https://www.daraz.com.bd/catalog/?q=suzuki&_keyori=ss&from=input&spm=a2a0e.home.search.go.73521b94tVpPwl",
        "https://bikroy.com/en/ads?query=Suzuki%20bike",
        "https://www.bagdoom.com/catalogsearch/result/?q=suzuki",
                "https://www.daraz.com.bd/catalog/?q=suzuki&_keyori=ss&from=input&spm=a2a0e.home.search.go.73521b94tVpPwl"};
        Integer [] Images = new Integer[]{R.drawable.daraz,R.drawable.bikroy,R.drawable.bagdoom,R.drawable.ekhanei};


        pdEcommerce.dismiss();
        for(int i=0;i<Images.length;i++){
            EcommerceLinks ecommerceLinks= new EcommerceLinks();
            ecommerceLinks.setTestImage(Images[i]);
            ecommerceLinks.setUrl(links[i]);
            ecommerceLinks.setImage(links[i]);

            arrayListOfEcommerceLinks.add(ecommerceLinks);
        }


        EcommerceListRecyclerAdapter customAdapter = new EcommerceListRecyclerAdapter(activity, arrayListOfEcommerceLinks);
        recyclerView.setAdapter(customAdapter);
*/


        //static end




        return view;
    }

    private void loadEcommerce() {

        if(pdEcommerce==null){
            pdEcommerce = new ProgressDialog(activity);
            pdEcommerce.setMessage("Loading .. ");
            pdEcommerce.setCancelable(false);
            pdEcommerce.setCanceledOnTouchOutside(false);
            pdEcommerce.show();
        }

        getUrlInstance(ECOMMERCE_API,new CustomCallBack(pdEcommerce,activity) {
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
                    pdEcommerce.dismiss();
                    backgroundThreadShortToast(activity, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray EcommerceLinksArray = null;
                    final List<EcommerceLinks> arrayListOfEcommerceLinks = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        EcommerceLinksArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < EcommerceLinksArray.length(); i++) {

                            JSONObject EcommerceLinksArrayObject = null;

                            EcommerceLinksArrayObject = EcommerceLinksArray.getJSONObject(i);
                            EcommerceLinks singleEcommerceLinks = new EcommerceLinks();
                            singleEcommerceLinks.setUrl(EcommerceLinksArrayObject.getString("ecommerce_url"));
                            singleEcommerceLinks.setImage(EcommerceLinksArrayObject.getString("image"));






                            arrayListOfEcommerceLinks.add(singleEcommerceLinks);


                        }


                    } catch (JSONException e) {
                        pdEcommerce.dismiss();
                        backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdEcommerce.dismiss();
                            EcommerceListRecyclerAdapter customAdapter = new EcommerceListRecyclerAdapter(activity, arrayListOfEcommerceLinks);
                            recyclerView.setAdapter(customAdapter);
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pdEcommerce.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEcommerce();
    }
}
