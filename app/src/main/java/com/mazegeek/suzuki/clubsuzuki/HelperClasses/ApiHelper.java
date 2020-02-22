/*
 * Created by Alvi Khalil on 10/30/18 11:47 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/30/18 11:47 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiHelper {
    public static ApiHelper apiHelper;
    public static ConnectionDetector connectionDetector;

    public static OkHttpClient client= new OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();




    private ApiHelper(){

    }
    public static synchronized ApiHelper getInstancemain(){
        if(apiHelper ==null){
            apiHelper = new ApiHelper();
        }


        return apiHelper;
    }


    public static void getUrlInstance(String url, final CustomCallBack customCallBack){
        if(client ==null){
            client = new OkHttpClient();
        }
        if(connectionDetector==null){
            connectionDetector = new ConnectionDetector(MyApplication.getAppContext());
        }
        if (!connectionDetector.isConnected()) {

            Intent intent=new Intent(MyApplication.getAppContext(),NoInternet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(intent);

        }
        else {


            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            client.newCall(request).enqueue(customCallBack);
        }

    }

    public static void getUrlInstance(String url,String headerName,String headerValue, final CustomCallBack customCallBack){
        if(client ==null){
            client = new OkHttpClient();
        }

        if(connectionDetector==null){
            connectionDetector = new ConnectionDetector(MyApplication.getAppContext());
        }
        if (!connectionDetector.isConnected()) {
            Intent intent=new Intent(MyApplication.getAppContext(),NoInternet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(intent);

        }
        else {


            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader(headerName, "Bearer " + headerValue)
                    .build();
            client.newCall(request).enqueue(customCallBack);
        }

    }

    public static void getUrlInstance(String url, RequestBody formBody, final CustomCallBack customCallBack){
        if(client ==null){
            client = new OkHttpClient();
        }

        if(connectionDetector==null){
            connectionDetector = new ConnectionDetector(MyApplication.getAppContext());
        }
        if (!connectionDetector.isConnected()) {
            Intent intent=new Intent(MyApplication.getAppContext(),NoInternet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(intent);

        }
        else {


            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(customCallBack);
        }

    }
    public static void getUrlInstance(String url, RequestBody formBody,String headerName,String headerValue, final CustomCallBack customCallBack){
        if(client ==null){
            client = new OkHttpClient();
        }

        if(connectionDetector==null){
            connectionDetector = new ConnectionDetector(MyApplication.getAppContext());
        }
        if (!connectionDetector.isConnected()) {
            Intent intent=new Intent(MyApplication.getAppContext(),NoInternet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(intent);

        }
        else {


            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader(headerName, "Bearer " + headerValue)
                    .build();

            client.newCall(request).enqueue(customCallBack);
        }

    }



    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
