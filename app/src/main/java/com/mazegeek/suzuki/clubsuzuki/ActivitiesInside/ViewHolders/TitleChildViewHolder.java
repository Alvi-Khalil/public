/*
 * Created by Alvi Khalil on 11/12/18 9:48 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/12/18 9:48 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.ViewHolders;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;


import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ExpandableChild;
import com.mazegeek.suzuki.clubsuzuki.R;

public class TitleChildViewHolder extends ChildViewHolder {

    public TextView textView1,textView2;
    public ConstraintLayout constraintLayout;
    public TitleChildViewHolder(View itemView) {
        super(itemView);
        textView1 = itemView.findViewById(R.id.text_specification_name);
        textView2 = itemView.findViewById(R.id.text_specification_value);
        constraintLayout = itemView.findViewById(R.id.cons_child);
    }

    public void bind(ExpandableChild expandableChild) {

        textView1.setText(expandableChild.getName());
        textView2.setText(expandableChild.getValue());
    }


}
