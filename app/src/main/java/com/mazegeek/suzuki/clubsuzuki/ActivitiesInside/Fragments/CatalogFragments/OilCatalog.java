/*
 * Created by Alvi Khalil on 11/1/18 9:55 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/1/18 9:55 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MapListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MyCatalogAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.OilListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeCard;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OilClass;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment;
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

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE_CATALOG_WITH_TYPE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.OIL_CATALOG_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.OIL_CATALOG_BIKE_ID_API;


public class OilCatalog extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = OilCatalog.class.getSimpleName();

    private static final String TAG = "checkaa";
    Activity activity;
    String myResponse;
    RecyclerView recyclerview;

    ProgressBar progressBar;
    ProgressBar progressBarEdit;

    ConstraintLayout moreConstraintLayout;

    ImageView drawerOpener,search;
    Boolean hasMore=false;

    Integer pageNumber=1,perPage=4;

    List<OilClass> arrayListOfOil;
    String[] names ;
    String[] ids;
    AutoCompleteTextView autoCompleteTextView;
    String bikeID;

    boolean firstTime=true;

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
        View view = inflater.inflate(R.layout.fragment_catalogs_oil, container, false);

        ((MainActivity) activity).hideKeyboard();
        moreConstraintLayout=view.findViewById(R.id.more);
        search=view.findViewById(R.id.search_logo);
        progressBarEdit = view.findViewById(R.id.edit_progressbar);
        autoCompleteTextView = view.findViewById(R.id.auto_com_text);

        recyclerview=view.findViewById(R.id.recycler1);
        recyclerview.setLayoutManager(new GridLayoutManager(activity, 2));

        progressBar = view.findViewById(R.id.pb_oils);

        arrayListOfOil = new ArrayList<>();



        moreConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber++;
                oilGetter(""+pageNumber,""+perPage);
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String input = autoCompleteTextView.getText().toString();


                for(int i=0;i<names.length;i++){
                    if(input.equalsIgnoreCase(names[i])){


                        bikeID=ids[i];

                       // Toast.makeText(activity, bikeID, Toast.LENGTH_SHORT).show();
                       loadOilsBike(bikeID);
                        break;
                    }
                }

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = autoCompleteTextView.getText().toString();
                if(input.equals("")){
                    Toast.makeText(activity, "Empty search bar!", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean isPresent = false;
                    int index=0;
                    for(int i=0;i<names.length;i++){
                        if(input.equalsIgnoreCase(names[i])){
                            isPresent=true;
                            index=i;
                            break;
                        }
                    }
                    if(isPresent){

                        bikeID=ids[index];

                        //Toast.makeText(activity, bikeID, Toast.LENGTH_SHORT).show();
                        loadOilsBike(bikeID);

                    }
                    else {
                        Toast.makeText(activity, "Invalid bike model!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });







        return view;
    }



    private void dropDownLoader() {
        getUrlInstance(BIKE_CATALOG_WITH_TYPE_API, new CustomCallBack(progressBarEdit,activity) {
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

                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        dataObject = new JSONObject(fileObject.getString("data"));
                        SportsArray = dataObject.getJSONArray("Sports");
                        CruiserArray = dataObject.getJSONArray("Cruiser");
                        CommuterArray = dataObject.getJSONArray("Commuter");
                        ScooterArray = dataObject.getJSONArray("Scooter");


                        names= new String[SportsArray.length()+CruiserArray.length()+CommuterArray.length()+ScooterArray.length()];
                        ids= new String[SportsArray.length()+CruiserArray.length()+CommuterArray.length()+ScooterArray.length()];
                        int index_of_names=0;

                        for (int i = 0; i < SportsArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = SportsArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));

                            names[index_of_names]=BikeArrayObject.getString("title");
                            ids[index_of_names]=BikeArrayObject.getString("id");
                            index_of_names++;
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

                            names[index_of_names]=BikeArrayObject.getString("title");
                            ids[index_of_names]=BikeArrayObject.getString("id");
                            index_of_names++;
                            arrayListOfSports.add(singleBikeCard);

                        }

                        for (int i = 0; i < CommuterArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = CommuterArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));

                            names[index_of_names]=BikeArrayObject.getString("title");
                            ids[index_of_names]=BikeArrayObject.getString("id");
                            index_of_names++;
                            arrayListOfSports.add(singleBikeCard);

                        }

                        for (int i = 0; i < ScooterArray.length(); i++) {

                            JSONObject BikeArrayObject = null;

                            BikeArrayObject = ScooterArray.getJSONObject(i);
                            BikeCard singleBikeCard = new BikeCard();
                            singleBikeCard.setTitle(BikeArrayObject.getString("title"));
                            singleBikeCard.setImage(BikeArrayObject.getString("image"));
                            singleBikeCard.setDescription(BikeArrayObject.getString("description"));
                            singleBikeCard.setPrice(BikeArrayObject.getString("price"));

                            names[index_of_names]=BikeArrayObject.getString("title");
                            ids[index_of_names]=BikeArrayObject.getString("id");
                            index_of_names++;
                            arrayListOfSports.add(singleBikeCard);

                        }





                    } catch (JSONException e) {
                        //progressBarEdit.setVisibility(View.GONE);
                        LocationFragment.backgroundThreadShortToast(activity,"Something went wrong fetching oil catalog!");
                        //LocationFragment.backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBarEdit.setVisibility(View.GONE);
                            autoCompleteTextView.setVisibility(View.VISIBLE);
                            if(names!=null){
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.suggestions_item,R.id.text_suggest,names);
                                autoCompleteTextView.setAdapter(adapter);
                            }



                        }
                    });


                } else {
                    progressBarEdit.setVisibility(View.GONE);
                    LocationFragment.backgroundThreadShortToast(activity,"Error!");

                }
            }


        });

    }


    private void loadOilsBike(String bikeID) {

        arrayListOfOil = new ArrayList<>();
        recyclerview.setVisibility(View.GONE);
        moreConstraintLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ((MainActivity) activity).hideKeyboard();

        RequestBody formBody = new FormBody.Builder()
                .add("bike_model_id", bikeID)
                .build();
        getUrlInstance(OIL_CATALOG_BIKE_ID_API,formBody, new CustomCallBack(progressBar,activity) {
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
                    JSONArray oilArray = null;

                    try {
                        fileObject = new JSONObject(myResponse);

                        oilArray = fileObject.getJSONArray("data");



                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        backgroundThreadShortToast(getActivity(), "Server Error!");
                        e.printStackTrace();
                    }



                    for (int i = 0; i < oilArray.length(); i++) {



                        JSONObject oilArrayObject = null;
                        try {

                            oilArrayObject = oilArray.getJSONObject(i);


                            OilClass singleOil = new OilClass();
                            singleOil.setName(oilArrayObject.getString("name"));
                            singleOil.setImage(oilArrayObject.getString("image"));
                            singleOil.setDescription(oilArrayObject.getString("description"));
                            singleOil.setPrice(oilArrayObject.getString("price"));


                            arrayListOfOil.add(singleOil);

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            backgroundThreadShortToast(getActivity(), "Server Error!");
                            e.printStackTrace();
                        }


                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);


                            OilListRecyclerAdapter customAdapter = new OilListRecyclerAdapter(activity, arrayListOfOil);
                            recyclerview.setAdapter(customAdapter);


                            recyclerview.setVisibility(View.VISIBLE);




                        }
                    });


                } else {
                    progressBar.setVisibility(View.GONE);
                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });

    }

    private void oilGetter(String pageNumber, String perPage) {

        ((MainActivity) activity).hideKeyboard();
        RequestBody formBody = new FormBody.Builder()
                .add("page_number", pageNumber)
                .add("per_page", perPage)
                .build();
        getUrlInstance(OIL_CATALOG_API,formBody, new CustomCallBack(progressBar,activity) {
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
                    JSONObject paginationObject = null;
                    JSONArray oilArray = null;

                    try {
                        fileObject = new JSONObject(myResponse);
                        paginationObject = new JSONObject(fileObject.getString("pagination"));
                        oilArray = fileObject.getJSONArray("data");

                        hasMore =paginationObject.getBoolean("has_more_pages");

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        backgroundThreadShortToast(getActivity(), "Server Error!");
                        e.printStackTrace();
                    }



                    for (int i = 0; i < oilArray.length(); i++) {



                        JSONObject oilArrayObject = null;
                        try {

                            oilArrayObject = oilArray.getJSONObject(i);


                            OilClass singleOil = new OilClass();
                            singleOil.setName(oilArrayObject.getString("name"));
                            singleOil.setImage(oilArrayObject.getString("image"));
                            singleOil.setDescription(oilArrayObject.getString("description"));
                            singleOil.setPrice(oilArrayObject.getString("price"));


                            arrayListOfOil.add(singleOil);

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            backgroundThreadShortToast(getActivity(), "Server Error!");
                            e.printStackTrace();
                        }


                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            firstTime=false;

                            OilListRecyclerAdapter customAdapter = new OilListRecyclerAdapter(activity, arrayListOfOil);
                            recyclerview.setAdapter(customAdapter);

                            if(hasMore){
                                moreConstraintLayout.setVisibility(View.VISIBLE);
                            }
                            else{
                                moreConstraintLayout.setVisibility(View.GONE);
                            }

                        }
                    });


                } else {
                    progressBar.setVisibility(View.GONE);
                    backgroundThreadShortToast(activity,"Error!");

                }
            }


        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if(firstTime){
            oilGetter("",""+perPage);
            dropDownLoader();
        }

    }

}
