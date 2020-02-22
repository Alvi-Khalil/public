/*
 *
 *  Created by Alvi Khalil on 11/28/18 4:40 PM
 *  Copyright (c) 2018 . All rights reserved.
 *  Last modified 11/28/18 4:40 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesBeforeLogin.SignUp;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.RideHistoryRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.ServiceRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.SoSListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.RideHistory;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ServiceScheduleItem;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.RideHistoryCardDelete;
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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.RIDE_HISTORY_DELETE;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SERVICE_LIST_SPECIFIC;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SOS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.USER_RIDE_HISTORY;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;

public class RideHistoryActivity extends AppCompatActivity implements RideHistoryCardDelete {

    RecyclerView recyclerView;
    ImageView back;
    ProgressDialog progressDialogRideHistory;
    String myResponse,message;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        activity=this;
        recyclerView=findViewById(R.id.ride_history_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));




        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




    }

    private void loadHistory() {
        if(progressDialogRideHistory==null){
            progressDialogRideHistory = new ProgressDialog(this);
            progressDialogRideHistory.setMessage("Loading .. ");
            progressDialogRideHistory.setCancelable(false);
            progressDialogRideHistory.setCanceledOnTouchOutside(false);
            progressDialogRideHistory.show();
        }

        String token = getMyPreference(this,TOKEN_OF_SEESION);

        RequestBody formBody = new FormBody.Builder()
                .add("page_number", "1")
                .add("per_page", "100")
                .build();


        getUrlInstance(USER_RIDE_HISTORY,formBody,"Authorization",token,new CustomCallBack(progressDialogRideHistory,this) {
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
                    progressDialogRideHistory.dismiss();
                    backgroundThreadShortToast(RideHistoryActivity.this, "Server Error!");
                    e.printStackTrace();
                }


//                    Log.d("collecd", "onResponse " + myResponse);
                if (response.isSuccessful()) {

                    JSONObject fileObject = null;
                    JSONArray RDArray = null;
                    final List<RideHistory> arrayListOfRD = new ArrayList<>();
                    try {
                        Log.d("collecd", "onResponse " + myResponse);
                        fileObject = new JSONObject(myResponse);
                        RDArray = fileObject.getJSONArray("data");



                        //pathN=""+pathName;
                        for (int i = 0; i < RDArray.length(); i++) {

                            JSONObject RDArrayObject = null;

                            RDArrayObject = RDArray.getJSONObject(i);
                            RideHistory rideHistory= new RideHistory();
                            rideHistory.setStartTime(RDArrayObject.getString("start_time"));
                            rideHistory.setStartLocation(RDArrayObject.getString("start_location"));
                            rideHistory.setEndTime(RDArrayObject.getString("end_time"));
                            rideHistory.setEndLocation(RDArrayObject.getString("end_location"));
                            rideHistory.setImage(RDArrayObject.getString("image"));
                            rideHistory.setDistance(RDArrayObject.getString("distance"));
                            rideHistory.setId(RDArrayObject.getString("id"));








                            arrayListOfRD.add(rideHistory);


                        }


                    } catch (JSONException e) {
                        progressDialogRideHistory.dismiss();
                        backgroundThreadShortToast(RideHistoryActivity.this, ""+e);
                        e.printStackTrace();
                    }


                    RideHistoryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialogRideHistory.dismiss();
                            RideHistoryRecyclerAdapter customAdapter = new RideHistoryRecyclerAdapter(RideHistoryActivity.this, arrayListOfRD,RideHistoryActivity.this);
                            recyclerView.setAdapter(customAdapter);
                            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    progressDialogRideHistory.dismiss();

                    backgroundThreadShortToast(RideHistoryActivity.this, message);
                }
            }


        });

    }

    private void launchDismissDlg(final String id) {

        final Dialog dialog = new Dialog(RideHistoryActivity.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_ride_history_delete);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView okBtn =  dialog.findViewById(R.id.yes);
        TextView cancelBtn =  dialog.findViewById(R.id.no);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RequestBody formBody = new FormBody.Builder()
                        .add("id",id )
                        .build();
                func2(formBody);
                //Toast.makeText(RideHistoryActivity.this, id, Toast.LENGTH_SHORT).show();
            // call delection api
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });
        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void func2(RequestBody formBody){
        final ProgressDialog pdEcommerce=new ProgressDialog(this);
        String token = getMyPreference(activity,TOKEN_OF_SEESION);
        LogPrint("setol",token);

        getUrlInstance(RIDE_HISTORY_DELETE,formBody,"Authorization",token,new CustomCallBack(pdEcommerce,activity) {
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








                    } catch (JSONException e) {
                        pdEcommerce.dismiss();
                        backgroundThreadShortToast(activity, ""+e);
                        e.printStackTrace();
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pdEcommerce.dismiss();

                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            loadHistory();


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
    public void onSelectionId(String id) {
        launchDismissDlg(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }
}
