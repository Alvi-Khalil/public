/*
 * Created by Alvi Khalil on 11/7/18 12:10 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/7/18 12:10 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.EcommerceListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.ServiceRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.EcommerceLinks;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ServiceScheduleItem;
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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_SURVICES;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ECOMMERCE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_SURVICE_HEADER;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.FREE_SURVICES;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.PAID_SURVICES;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SERVICE_LIST_ALL;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SERVICE_LIST_SPECIFIC;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;

public class ServiceSchedule extends AppCompatActivity {


    ImageView backBtn;

    RecyclerView recyclerView;

    ProgressDialog pdEcommerce;

    String myResponse,message;
    Activity activity;
    String pageName;
    TextView textViewTopHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_scedule);

        activity=this;


        Intent in = getIntent();
        pageName=in.getStringExtra(EXTRA_SURVICE_HEADER);



        backBtn = findViewById(R.id.back);
        textViewTopHeader = findViewById(R.id.top_header);
        textViewTopHeader.setText(pageName);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));




        //static start

       /* List<ServiceScheduleItem> arrayListOfService = new ArrayList<>();
        String [] names = new String[]{"Free Service 1","Free Service 2","Free Service 3","Free Service 4","Free Service 5","Free Service 6","Free Service 7","Free Service 8","Free Service 9","Free Service 10","Free Service 11","Free Service 12"};
        String [] status = new String[]{"Used","Missed", "Missed","Used","Used","Up Coming","Pending","Pending","Pending","Pending","Pending","Pending"};
        String [] dates = new String[]{"10/12/18","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19","12/01/19"};



        for(int i=0;i<names.length;i++){
            ServiceScheduleItem serviceScheduleItem = new ServiceScheduleItem();

            serviceScheduleItem.setServiceName(names[i]);
            serviceScheduleItem.setStatus(status[i]);



            //LogPrint("Kiklo",numbers[i]+" "+headers[i]+" "+Images[i]+"\n");

            arrayListOfService.add(serviceScheduleItem);
        }


        ServiceRecyclerAdapter customAdapter = new ServiceRecyclerAdapter(this, arrayListOfService);
        recyclerView.setAdapter(customAdapter);*/


        //static end



    }

    public void func(){
        if(pdEcommerce==null){
            pdEcommerce = new ProgressDialog(activity);
            pdEcommerce.setMessage("Loading .. ");
            pdEcommerce.setCancelable(false);
            pdEcommerce.setCanceledOnTouchOutside(false);
            pdEcommerce.show();
        }

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(SERVICE_LIST_ALL,"Authorization",token,new CustomCallBack(pdEcommerce,activity) {
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
                    JSONArray ServicesArray = null;
                    final List<ServiceScheduleItem> arrayListOfService = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        ServicesArray = fileObject.getJSONArray("data");



                        for (int i = 0; i < ServicesArray.length(); i++) {

                            JSONObject ServiceArrayObject = null;

                            ServiceArrayObject = ServicesArray.getJSONObject(i);
                            ServiceScheduleItem singleServiceScheduleItem = new ServiceScheduleItem();
                            singleServiceScheduleItem.setServiceName(ServiceArrayObject.getString("name"));
                            singleServiceScheduleItem.setServiceStartDate(ServiceArrayObject.getString("start_date")+" to "+ServiceArrayObject.getString("end_date"));
                            singleServiceScheduleItem.setServiceEndDate(ServiceArrayObject.getString("end_date"));
                            singleServiceScheduleItem.setStatus(ServiceArrayObject.getString("status"));
                            singleServiceScheduleItem.setMileage("Mileage : "+ServiceArrayObject.getString("mileage"));






                            arrayListOfService.add(singleServiceScheduleItem);


                        }







                    } catch (JSONException e) {
                        pdEcommerce.dismiss();
                        backgroundThreadShortToast(activity, ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdEcommerce.dismiss();
                            ServiceRecyclerAdapter customAdapter = new ServiceRecyclerAdapter(activity, arrayListOfService);
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


    public void func2(RequestBody formBody){

        if(pdEcommerce==null){
            pdEcommerce = new ProgressDialog(activity);
            pdEcommerce.setMessage("Loading .. ");
            pdEcommerce.setCancelable(false);
            pdEcommerce.setCanceledOnTouchOutside(false);
            pdEcommerce.show();
        }

        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(SERVICE_LIST_SPECIFIC,formBody,"Authorization",token,new CustomCallBack(pdEcommerce,activity) {
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
                    JSONArray ServicesArray = null;
                    final List<ServiceScheduleItem> arrayListOfService = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        ServicesArray = fileObject.getJSONArray("data");



                        for (int i = 0; i < ServicesArray.length(); i++) {

                            JSONObject ServiceArrayObject = null;

                            ServiceArrayObject = ServicesArray.getJSONObject(i);
                            ServiceScheduleItem singleServiceScheduleItem = new ServiceScheduleItem();
                            singleServiceScheduleItem.setServiceName(ServiceArrayObject.getString("name"));
                            singleServiceScheduleItem.setServiceStartDate(ServiceArrayObject.getString("start_date")+" to "+ServiceArrayObject.getString("end_date"));
                            singleServiceScheduleItem.setServiceEndDate(ServiceArrayObject.getString("end_date"));
                            singleServiceScheduleItem.setStatus(ServiceArrayObject.getString("status"));
                            singleServiceScheduleItem.setMileage("Mileage : "+ServiceArrayObject.getString("mileage"));






                            arrayListOfService.add(singleServiceScheduleItem);


                        }







                    } catch (JSONException e) {
                        pdEcommerce.dismiss();
                        backgroundThreadShortToast(activity, ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdEcommerce.dismiss();
                            ServiceRecyclerAdapter customAdapter = new ServiceRecyclerAdapter(activity, arrayListOfService);
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
    protected void onResume() {
        super.onResume();
        if(pageName.equals(ALL_SURVICES)){
            func();
        }
        else if(pageName.equals(FREE_SURVICES)){
            RequestBody formBody = new FormBody.Builder()
                    .add("service_id","1" )
                    .build();
            func2(formBody);
        }
        else if(pageName.equals(PAID_SURVICES)){
            RequestBody formBody = new FormBody.Builder()
                    .add("service_id","2" )
                    .build();
            func2(formBody);
        }
    }
}
