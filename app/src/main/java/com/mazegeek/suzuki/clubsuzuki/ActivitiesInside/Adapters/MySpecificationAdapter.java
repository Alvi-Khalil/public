/*
 * Created by Alvi Khalil on 11/12/18 10:03 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/12/18 10:03 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ExpandableChild;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ExpandableParent;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.ViewHolders.TitleChildViewHolder;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.ViewHolders.TitleParentViewHolder;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.Helper.LogPrint;

public class MySpecificationAdapter extends ExpandableRecyclerAdapter<ExpandableParent, ExpandableChild,TitleParentViewHolder,TitleChildViewHolder> {

    LayoutInflater mInflater;
    Context mContextt;
    ImageView imageView1;
    TitleParentViewHolder titleParentViewHolder1;
    boolean isCollapsed=true;
    List<ExpandableParent> parentItemList;


    public MySpecificationAdapter(Context context, List<ExpandableParent> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
        this.mContextt=context;
        this.parentItemList = parentItemList;
    }







 /*   @Override
    public void onBindParentViewHolder(final TitleParentViewHolder titleParentViewHolder, final int position,final Object o) {


        ExpandableParent title =(ExpandableParent)o;
        titleParentViewHolder.textView.setText(title.getTitle());


        titleParentViewHolder1 =titleParentViewHolder;
        imageView1 =titleParentViewHolder.imageView;
        *//*if(!title.isExpanded()){
            titleParentViewHolder.imageView.setImageResource(R.drawable.up_arrow);
            title.setExpanded(true);
        }
        else
        {
            titleParentViewHolder.imageView.setImageResource(R.drawable.more_images);
            title.setExpanded(false);
        }*//*
        titleParentViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MySpecificationAdapter.this.onParentItemClickListener(position);
                LogPrint("Checkk"," "+!titleParentViewHolder.isExpanded()+" "+position);

                if(!titleParentViewHolder.isExpanded()){
                    titleParentViewHolder.imageView.setImageResource(R.drawable.up_arrow);
                    titleParentViewHolder.setExpanded(true);
                }
                else
                {
                    titleParentViewHolder.imageView.setImageResource(R.drawable.more_images);
                    titleParentViewHolder.setExpanded(false);
                }

            }
        });


    }
*/
    /*@Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int position, Object o) {
        ExpandableChild title =(ExpandableChild)o;
        titleChildViewHolder.textView1.setText(title.getName());
        titleChildViewHolder.textView2.setText(title.getValue());



        if(position%2==0){
            //titleChildViewHolder.constraintLayout.setBackgroundColor(ContextCompat.getColor(mContextt, R.color.white));
        }
    }*/

   /* @Override
    public void onParentItemClickListener(int position) {
        super.onParentItemClickListener(position);
        Toast.makeText(mContextt, "hss "+position, Toast.LENGTH_SHORT).show();
        //ExpandableParent title = (ExpandableParent) parentItemList.get(position);
        //notifyDataSetChanged();
      *//*  if(titleParentViewHolder1.isExpanded()){
            Toast.makeText(mContextt, "hss1", Toast.LENGTH_SHORT).show();
            //imageView1.setImageResource(R.drawable.up_arrow);
        }
        else{
            Toast.makeText(mContextt, "hss2", Toast.LENGTH_SHORT).show();
            //imageView1.setImageResource(R.drawable.more_images);
        }*//*
      *//*  if(isCollapsed){
            Toast.makeText(mContextt, "expand", Toast.LENGTH_SHORT).show();
            imageView1.setImageResource(R.drawable.up_arrow);
            //titleParentViewHolder1.setExpanded(true);
            isCollapsed=false;
        }
        else
        {
            Toast.makeText(mContextt, "collapse", Toast.LENGTH_SHORT).show();
            imageView1.setImageResource(R.drawable.more_images);
            //titleParentViewHolder1.setExpanded(false);
            isCollapsed=true;
        }*//*


    }*/


    @NonNull
    @Override
    public TitleParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view=mInflater.inflate(R.layout.specification_parent,parentViewGroup,false);

        return new TitleParentViewHolder(view);
    }

    @NonNull
    @Override
    public TitleChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view=mInflater.inflate(R.layout.specification_child,childViewGroup,false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull final TitleParentViewHolder parentViewHolder, final int parentPosition, @NonNull final ExpandableParent parent) {
        parentViewHolder.bind(parent);

        if(!parent.isExpanded()){
            parentViewHolder.imageView.setImageResource(R.drawable.more_images);
        }
        else{
            parentViewHolder.imageView.setImageResource(R.drawable.up_arrow);
        }
        parentViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MySpecificationAdapter.this.onParentItemClickListener(position);
                //LogPrint("Checkk"," "+!titleParentViewHolder.isExpanded()+" "+position);



                if(!parent.isExpanded()){
                    parentViewHolder.imageView.setImageResource(R.drawable.up_arrow);
                    expandParent(parentPosition);
                    parent.setExpanded(true);
                }
                else
                {
                    parentViewHolder.imageView.setImageResource(R.drawable.more_images);
                    collapseParent(parentPosition);
                    parent.setExpanded(false);
                }

            }
        });


    }

    @Override
    public void onBindChildViewHolder(@NonNull TitleChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull ExpandableChild child) {
        childViewHolder.bind(child);

        /*if(childPosition%2==0){
            childViewHolder.constraintLayout.setBackgroundColor(ContextCompat.getColor(mContextt, R.color.white));
        }*/
    }
}
