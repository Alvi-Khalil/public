/*
 * Created by Alvi Khalil on 10/31/18 3:28 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/31/18 3:28 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.HelperClasses;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mazegeek.suzuki.clubsuzuki.R;

public class ImageLoaderGlide {

    public static void gluideLoader(Context context, String urlOfImage, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading33)
                .error(R.drawable.no_image33);

        Glide.with(context).load(urlOfImage).apply(options).into(imageView);
    }
}
