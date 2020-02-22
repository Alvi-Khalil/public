/*
 * Created by Alvi Khalil on 11/12/18 9:24 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/12/18 9:24 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;



import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;
import java.util.UUID;

public class ExpandableParent implements Parent<ExpandableChild> {


    private List<ExpandableChild> mChildrenList;
    private String title;
    private boolean isExpanded = false;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public List<ExpandableChild> getChildList() {
        return mChildrenList;
    }

    public void setChildObjectList(List<ExpandableChild> list) {
        mChildrenList =list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
