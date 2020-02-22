/*
 * Created by Alvi Khalil on 11/2/18 2:49 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 12:42 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Options;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.OptionSelectionOfSurvey;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.Survey.MULTIPLE_CHOICE;
import static com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.Survey.SINGLE_CHOICE;


public class MultipleListRecyclerAdapter extends RecyclerView.Adapter<MultipleListRecyclerAdapter.CustomViewHolder> {
    private List<Options> feedItemList;
    private Context mContext;
    private String quesType;
    private int selectedPosition=-1,oldPosition=-1;
    private OptionSelectionOfSurvey mCallback;
    private boolean nothingTouched=true;



    public MultipleListRecyclerAdapter(Context context, List<Options> feedItemList,String quesType,OptionSelectionOfSurvey mCallback) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.quesType = quesType;
        this.mCallback = mCallback;



    }

    @Override
    public int getItemViewType(int position) {

        if(quesType.equals(SINGLE_CHOICE)){
            return 1;
        }
        else if(quesType.equals(MULTIPLE_CHOICE)){
            return 2;
        }

        return 0;


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;
            case 2:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_list_view2, parent, false);
                CustomViewHolder row2 = new CustomViewHolder(view2,1);
                return row2;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        final Options dc_list = feedItemList.get(position);


        if(quesType.equals(MULTIPLE_CHOICE)&&nothingTouched){
            dc_list.setSelected(false);
        }
        if(oldPosition!=-1){
            if(position==oldPosition){
                holder.imageBlue.setVisibility(View.GONE);
                holder.imageCheck.setVisibility(View.GONE);
            }
        }
            //holder.image.setImageResource(dc_list.getTestImage());
            //gluideLoader(mContext,dc_list.getImage(),holder.image);

            holder.header.setText(dc_list.getOptionText());

            holder.imageGrey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nothingTouched=false;
                    if(quesType.equals(SINGLE_CHOICE)){

                        if(selectedPosition!=-1){
                            oldPosition=selectedPosition;

                            notifyItemChanged(oldPosition);
                        }

                        selectedPosition=position;
                        mCallback.onClickOption(""+dc_list.getOptionId());
                        holder.imageBlue.setVisibility(View.VISIBLE);
                        holder.imageCheck.setVisibility(View.VISIBLE);
                    }
                    else if(quesType.equals(MULTIPLE_CHOICE)){
                        dc_list.setSelected(true);
                        feedItemList.remove(position);
                        feedItemList.add(position,dc_list);

                        mCallback.onClickMultipleOption(feedItemList);
                        holder.imageBlue.setVisibility(View.VISIBLE);
                        holder.imageCheck.setVisibility(View.VISIBLE);
                    }


                }
            });
        holder.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesType.equals(SINGLE_CHOICE)&&selectedPosition==position){

                }else{
                    dc_list.setSelected(false);
                    feedItemList.remove(position);
                    feedItemList.add(position,dc_list);

                    mCallback.onClickMultipleOption(feedItemList);
                    holder.imageBlue.setVisibility(View.GONE);
                    holder.imageCheck.setVisibility(View.GONE);
                }


            }
        });









    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        ImageView imageGrey,imageCheck,imageBlue;
        TextView header;




        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            imageGrey = itemView.findViewById(R.id.image_grey);
            imageBlue = itemView.findViewById(R.id.image_blue);
            imageCheck = itemView.findViewById(R.id.image_check);
            header = itemView.findViewById(R.id.option_text);




        }
    }
}