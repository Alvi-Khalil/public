/*
 * Created by Alvi Khalil on 11/1/18 9:48 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/1/18 9:48 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments.BikeCatalog;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments.OilCatalog;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Fragments.CatalogFragments.PartsCatalog;

import java.util.List;


public class CatalogViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public CatalogViewPagerAdapter(FragmentManager fm, List<Fragment> fragments)
    {
        super(fm);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}