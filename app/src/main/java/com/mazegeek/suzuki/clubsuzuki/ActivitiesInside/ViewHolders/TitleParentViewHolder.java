/*
 * Created by Alvi Khalil on 11/12/18 9:42 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/12/18 9:42 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.ViewHolders;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ExpandableParent;
import com.mazegeek.suzuki.clubsuzuki.R;

public class TitleParentViewHolder extends ParentViewHolder {

    public TextView textView;
    public ImageView imageView;
    public ConstraintLayout constraintLayout;

    public TitleParentViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text_specification);
        imageView = itemView.findViewById(R.id.down_arrow);
        constraintLayout = itemView.findViewById(R.id.parent_cons);





    }

    public void bind(ExpandableParent expandableParent) {

        textView.setText(expandableParent.getTitle());



    }


}
