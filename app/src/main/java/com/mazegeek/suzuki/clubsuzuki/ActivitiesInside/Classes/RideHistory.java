/*
 *
 *  Created by Alvi Khalil on 11/28/18 4:39 PM
 *  Copyright (c) 2018 . All rights reserved.
 *  Last modified 11/28/18 4:39 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

public class RideHistory {
    private String startLocation;
    private String endLocation;
    private String startTime;
    private String endTime;
    private String image;
    private String distance;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
