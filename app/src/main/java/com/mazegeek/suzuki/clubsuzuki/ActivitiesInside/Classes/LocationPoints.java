/*
 * Created by Alvi Khalil on 10/29/18 4:40 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 9/12/18 7:31 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

public class LocationPoints {
    private String header;
    private String body;
    private int ImageID;
    private double logitude;
    private double latitude;
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setLogitude(double logitude) {
        this.logitude = logitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
