/*
 * Created by Alvi Khalil on 11/1/18 9:55 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/1/18 9:55 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MyCatalogAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.MyCatalogAdapterForParts;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.OilListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeCard;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.PartsClass;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OilClass;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.LocationFragment;
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

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.getUrlInstance;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE_CATALOG_WITH_TYPE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PART_CATALOG_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PART_CATALOG_BIKE_ID_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;


public class PartsCatalog extends Fragment {

    private static final String COMMON_TAG = "CombinedLifeCycle";
    private static final String FRAGMENT_NAME = PartsCatalog.class.getSimpleName();

    private static final String TAG = "checkaa";
    Activity activity;

    String myResponse;
    ProgressBar progressBarEngine,progressBarBody,progressBarAccessories;
    CustomeViewpager EnginemPager,BodymPager,AccessoriesmPager;

    ImageView drawerOpener;

    List<PartsClass> arrayListOfbodyEngine;
    List<PartsClass> arrayListOfbodyBody;
    List<PartsClass> arrayListOfbodyAccessories;
    MyCatalogAdapterForParts myCatalogAdapterForEngine;
    MyCatalogAdapterForParts myCatalogAdapterForBody;
    MyCatalogAdapterForParts myCatalogAdapterForAccessories;

    private static final String BODY ="body";
    private static final String ENGINE ="engine";
    private static final String ACCESSORIES ="accessories";

    TextView engineMore,bodyMore,accessoriedMore;
    Boolean hasMoreEngin=false,hasMoreBody=false,hasMoreAccessories=false;


    Integer enginPageNumber=1,bodyPageNumber=1,accessoriesPageNumber=1,perPage=4;

    String[] names ;
    String[] ids;
    AutoCompleteTextView autoCompleteTextView;
    String bikeID;
    ProgressBar progressBarEdit;
    ImageView search;

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
        View view = inflater.inflate(R.layout.fragment_catalogs_parts, container, false);
        ((MainActivity) activity).hideKeyboard();

        search=view.findViewById(R.id.search_logo);
        progressBarEdit = view.findViewById(R.id.edit_progressbar);
        autoCompleteTextView = view.findViewById(R.id.auto_com_text);

        progressBarAccessories = view.findViewById(R.id.accessories_progress);
        progressBarBody = view.findViewById(R.id.body_progress);
        progressBarEngine = view.findViewById(R.id.engine_progress);

        EnginemPager = view.findViewById(R.id.kk_pager);
        BodymPager = view.findViewById(R.id.kk_pager2);
        AccessoriesmPager = view.findViewById(R.id.kk_pager3);

        engineMore = view.findViewById(R.id.engine_more_text);
        bodyMore = view.findViewById(R.id.body_more_text);
        accessoriedMore = view.findViewById(R.id.accessories_more_text);


        arrayListOfbodyEngine = new ArrayList<>();
        arrayListOfbodyBody = new ArrayList<>();
        arrayListOfbodyAccessories = new ArrayList<>();

        myCatalogAdapterForEngine = new MyCatalogAdapterForParts(activity,arrayListOfbodyEngine);
        myCatalogAdapterForBody = new MyCatalogAdapterForParts(activity,arrayListOfbodyBody);
        myCatalogAdapterForAccessories = new MyCatalogAdapterForParts(activity,arrayListOfbodyAccessories);









        EnginemPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                visibilityOfMore(arrayListOfbodyEngine,EnginemPager,engineMore,hasMoreEngin);
            }
        });

        BodymPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                visibilityOfMore(arrayListOfbodyBody,BodymPager,bodyMore,hasMoreBody);
            }
        });

        AccessoriesmPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                visibilityOfMore(arrayListOfbodyAccessories,AccessoriesmPager,accessoriedMore,hasMoreAccessories);
            }
        });

        engineMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(activity, "works", Toast.LENGTH_SHORT).show();
                engineMore.setVisibility(View.GONE);
                enginPageNumber++;
                progressBarEngine.setVisibility(View.VISIBLE);
                loadParts(ENGINE,progressBarEngine,""+enginPageNumber,""+perPage);
            }
        });

        bodyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(activity, "works", Toast.LENGTH_SHORT).show();
                bodyMore.setVisibility(View.GONE);
                bodyPageNumber++;
                progressBarBody.setVisibility(View.VISIBLE);
                loadParts(BODY,progressBarBody,""+bodyPageNumber,""+perPage);
            }
        });

        accessoriedMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(activity, "works", Toast.LENGTH_SHORT).show();
                accessoriedMore.setVisibility(View.GONE);
                accessoriesPageNumber++;
                progressBarAccessories.setVisibility(View.VISIBLE);
                loadParts(ACCESSORIES,progressBarAccessories,""+accessoriesPageNumber,""+perPage);
            }
        });



        return view;
    }

    private void allBikes() {
        loadParts(BODY,progressBarBody,""+enginPageNumber,""+perPage);
        loadParts(ENGINE,progressBarEngine,""+bodyPageNumber,""+perPage);
        loadParts(ACCESSORIES,progressBarAccessories,""+accessoriesPageNumber,""+perPage);
    }

    private void visibilityOfMore(List<PartsClass> arrayList,CustomeViewpager mPAger,TextView more,Boolean hasMore) {
        if(arrayList.size()>0 && mPAger.getCurrentItem()+1==arrayList.size() && hasMore){

            more.setVisibility(View.VISIBLE);
        }
        else{
            more.setVisibility(View.GONE);
        }
    }

    private void loadParts(final String partType, final ProgressBar progressBar, final String pageNumber, String perPage) {

        RequestBody formBody = new FormBody.Builder()
                .add("part_type", partType)
                .add("page_number", pageNumber)
                .add("per_page", perPage)
                .build();

        getUrlInstance(PART_CATALOG_API,formBody, new CustomCallBack(progressBar,activity) {
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
                    JSONArray partArray = null;
                    JSONObject paginationObject = null;


                    try {
                        fileObject = new JSONObject(myResponse);
                        paginationObject = new JSONObject(fileObject.getString("pagination"));
                        partArray = fileObject.getJSONArray("data");
                        Boolean value;
                        value =paginationObject.getBoolean("has_more_pages");

                        if(partType.equals(ENGINE)){

                            hasMoreEngin =value;
                        }
                        else if(partType.equals(BODY)){
                            hasMoreBody =value;
                        }
                        else if(partType.equals(ACCESSORIES)){
                            hasMoreAccessories =value;
                        }


                    } catch (JSONException e) {
                        //progressBar.setVisibility(View.GONE);
                        backgroundThreadShortToast(getActivity(), "Server Error!");
                        e.printStackTrace();
                    }


                    final ArrayList<PartsClass> partsClasses = new ArrayList<>();
                    for (int i = 0; i < partArray.length(); i++) {



                        JSONObject partArrayObject = null;
                        try {

                            partArrayObject = partArray.getJSONObject(i);


                            PartsClass singlePart = new PartsClass();
                            singlePart.setName(partArrayObject.getString("name"));
                            singlePart.setImage(partArrayObject.getString("image"));
                            singlePart.setDescription(partArrayObject.getString("description"));
                            singlePart.setPrice(partArrayObject.getString("price"));
                            partsClasses.add(singlePart);



                        } catch (JSONException e) {
                            //progressBar.setVisibility(View.GONE);
                            LocationFragment.backgroundThreadShortToast(activity,"Something went wrong fetching parts catalog!");
                            e.printStackTrace();
                        }


                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            firstTime=false;
                            if(partType.equals(ENGINE) ){
                                if(pageNumber.equals("1")){
                                    EnginemPager.setAdapter(myCatalogAdapterForEngine);
                                    EnginemPager.setAnimationEnabled(true);
                                    EnginemPager.setFadeEnabled(true);
                                    EnginemPager.setFadeFactor(0.5f);
                                }
                                arrayListOfbodyEngine.addAll(partsClasses);
                                myCatalogAdapterForEngine.notifyDataSetChanged();




                            }
                            else if(partType.equals(BODY)){
                                if(pageNumber.equals("1")){
                                    BodymPager.setAdapter(myCatalogAdapterForBody);
                                    BodymPager.setAnimationEnabled(true);
                                    BodymPager.setFadeEnabled(true);
                                    BodymPager.setFadeFactor(0.5f);

                                }
                                arrayListOfbodyBody.addAll(partsClasses);
                                myCatalogAdapterForBody.notifyDataSetChanged();



                            }
                            else if(partType.equals(ACCESSORIES)){

                                if(pageNumber.equals("1")){
                                    AccessoriesmPager.setAdapter(myCatalogAdapterForAccessories);
                                    AccessoriesmPager.setAnimationEnabled(true);
                                    AccessoriesmPager.setFadeEnabled(true);
                                    AccessoriesmPager.setFadeFactor(0.5f);
                                }
                                arrayListOfbodyAccessories.addAll(partsClasses);
                                myCatalogAdapterForAccessories.notifyDataSetChanged();




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
                        LocationFragment.backgroundThreadShortToast(getActivity(), ""+e);
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBarEdit.setVisibility(View.GONE);
                            autoCompleteTextView.setVisibility(View.VISIBLE);
                            if(names!=null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.suggestions_item, R.id.text_suggest, names);
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

    @Override
    public void onResume() {
        super.onResume();

        if(firstTime){
            allBikes();
            dropDownLoader();
        }

    }
}
