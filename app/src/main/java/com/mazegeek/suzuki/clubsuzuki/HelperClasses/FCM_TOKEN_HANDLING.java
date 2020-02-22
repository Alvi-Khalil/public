/*
 * Created by Alvi Khalil on 10/31/18 3:28 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/31/18 3:28 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters.SoSListRecyclerAdapter;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;

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
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.FCM_TOKEN;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NEW_FCM_TOKEN;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.NOTIFICAION_TOKEN_UPDATE_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.SOS_API;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.TOKEN_OF_SEESION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.getMyPreference;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Preferences.setMyPreference;

public class FCM_TOKEN_HANDLING {

    private static final String TAG="FCMTokenhelperClass";
    private static final String ADD="adddd";
    private static final String DELETE="deletee";
    private static final String REPLACE="replaceee";
    String myResponse,message;
    Activity activity;
    String fcmToken;



  public void fcmTokenAdd(final Context context){

      FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
          @Override
          public void onSuccess(InstanceIdResult instanceIdResult) {
              fcmToken= instanceIdResult.getToken();
              LogPrint(TAG, "FCM adding: " + fcmToken);
              RequestBody formBody = new FormBody.Builder()
                      .add("new_notification_token", fcmToken)
                      .add("app_platform", "1")
                      .build();
              activity=(Activity)context;
              api(formBody,activity,ADD);
          }
      });


  }
    public void fcmTokenDelete(final Context context){
        activity=(Activity)context;
        fcmToken= getMyPreference(activity,FCM_TOKEN);
        LogPrint(TAG, "FCM deleting: " + fcmToken);
        RequestBody formBody = new FormBody.Builder()
                .add("old_notification_token", fcmToken)
                .build();

        api(formBody,activity,DELETE);


    }

    public void fcmTokenReplace(final Context context,final String newToken){
        activity=(Activity)context;


        fcmToken= getMyPreference(activity,FCM_TOKEN);

        //LogPrint(TAG, "FCM: " + fcmToken);
        RequestBody formBody = new FormBody.Builder()
                .add("new_notification_token", newToken)
                .add("app_platform", "1")
                .add("old_notification_token", fcmToken)
                .build();

        api(formBody,activity,REPLACE);

    }


    private void api(RequestBody formBody,final Activity activity,final String s) {
        final ProgressDialog pdSos=new ProgressDialog(activity);
        String token = getMyPreference(activity,TOKEN_OF_SEESION);

        LogPrint(TAG,"in api func : "+token);
        getUrlInstance(NOTIFICAION_TOKEN_UPDATE_API,formBody,"Authorization",token,new CustomCallBack(pdSos,activity) {
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


//
                if (response.isSuccessful()) {


                    LogPrint(TAG,s);
                    if(s.equals(ADD)){
                        LogPrint(TAG,"add hosse ki?");
                        setMyPreference(activity,FCM_TOKEN,fcmToken);
                    }
                    else if(s.equals(DELETE)){
                        LogPrint(TAG,"faka hosse ki?");
                        setMyPreference(activity,FCM_TOKEN,"");
                    }
                    else if(s.equals(REPLACE)){
                        LogPrint(TAG,"faka hosse ki?");
                        String temp =getMyPreference(activity,NEW_FCM_TOKEN);
                        setMyPreference(activity,FCM_TOKEN,temp);
                        setMyPreference(activity,NEW_FCM_TOKEN,"");
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            pdSos.dismiss();



                        }
                    });
                } else {
                    pdSos.dismiss();

                    backgroundThreadShortToast(activity, message);
                }
            }


        });
    }
}
