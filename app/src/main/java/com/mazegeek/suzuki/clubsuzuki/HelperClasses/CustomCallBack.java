/*
 * Created by Alvi Khalil on 10/30/18 1:38 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/30/18 1:38 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ApiHelper.backgroundThreadShortToast;

public abstract class CustomCallBack implements Callback {

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private Context context;
    public CustomCallBack(ProgressDialog progressDialog, Context context){
        this.progressDialog=progressDialog;
        this.context=context;
    }
    public CustomCallBack(ProgressBar progressBar, Context context){
        this.progressBar=progressBar;
        this.context=context;
    }
    @Override
    public void onFailure(Call call, IOException e) {

        //Log.d("MainActivity",e.getMessage());
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
//        if(progressBar!=null){
//            progressBar.setVisibility(View.GONE);
//        }

        backgroundThreadShortToast(context, ""+e);
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        sendResponse(response);
    }


    public abstract void sendResponse(Response response);
}
