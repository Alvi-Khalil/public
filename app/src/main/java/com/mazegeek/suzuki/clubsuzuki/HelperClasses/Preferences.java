/*
 * Created by Alvi Khalil on 10/31/18 3:28 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/31/18 3:28 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mazegeek.suzuki.clubsuzuki.R;


public class Preferences {


    public static void setMyPreference(Context context, String preferenceName, String preferenceValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.commit();
    }

    public static String getMyPreference(Context context,String preferenceName){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceValue = preferences.getString(preferenceName,"");
        return preferenceValue;
    }
}
