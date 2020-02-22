/*
 * Created by Alvi Khalil on 11/2/18 11:10 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 11:10 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Options implements Parcelable {
    private String optionText;
    private String optionId;
    private boolean isSelected;

    protected Options(Parcel in) {
        optionText = in.readString();
        optionId = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<Options> CREATOR = new Creator<Options>() {
        @Override
        public Options createFromParcel(Parcel in) {
            return new Options(in);
        }

        @Override
        public Options[] newArray(int size) {
            return new Options[size];
        }
    };

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public Options(){}



    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(optionText);
        parcel.writeString(optionId);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
