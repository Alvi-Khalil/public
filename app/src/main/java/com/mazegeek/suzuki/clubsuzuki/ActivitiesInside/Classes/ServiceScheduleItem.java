/*
 * Created by Alvi Khalil on 11/7/18 1:20 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/7/18 1:20 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

public class ServiceScheduleItem {

    private String serviceName;
    private String status;
    private String serviceStartDate;
    private String serviceEndDate;
    private String mileage;

    public String getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(String serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public String getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
