/*
 * Created by Alvi Khalil on 10/25/18 9:07 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/25/18 9:07 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.MainActivity;
import com.mazegeek.suzuki.clubsuzuki.R;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.HomeFragment.FIRST_PAGE;
import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.HomeFragment.LOOPS;
import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.HomeFragment.PAGES;

public class MyPagerAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;
    private MainActivity context;
    private FragmentManager fm;
    private float scale;

    public MyPagerAdapter(MainActivity context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        position = position % PAGES;
        return MyFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        return PAGES * LOOPS;
    }

    @Override
    public void transformPage(View page, float position) {
        MyLinearLayout myLinearLayout = (MyLinearLayout) page.findViewById(R.id.root);
        float scale = BIG_SCALE;
        if (position > 0) {
            scale = scale - position * DIFF_SCALE;
        } else {
            scale = scale + position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0;
        myLinearLayout.setScaleBoth(scale);
    }
}
