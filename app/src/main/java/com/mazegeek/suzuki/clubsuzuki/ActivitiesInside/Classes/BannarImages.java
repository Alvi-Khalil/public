/*
 * Created by Alvi Khalil on 10/31/18 12:47 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/31/18 12:47 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

import java.io.Serializable;

public class BannarImages implements Serializable {
    private String bannar;
    private String bannarText;
    private String bannarTitle;
    private String bannarSubtitle;
    private String bannarTime;
    private String bannarUrl;

    public String getBannarUrl() {
        return bannarUrl;
    }

    public void setBannarUrl(String bannarUrl) {
        this.bannarUrl = bannarUrl;
    }

    public String getBannarTime() {
        return bannarTime;
    }

    public void setBannarTime(String bannarTime) {
        this.bannarTime = bannarTime;
    }

    public String getBannarTitle() {
        return bannarTitle;
    }

    public void setBannarTitle(String bannarTitle) {
        this.bannarTitle = bannarTitle;
    }

    public String getBannarSubtitle() {
        return bannarSubtitle;
    }

    public void setBannarSubtitle(String bannarSubtitle) {
        this.bannarSubtitle = bannarSubtitle;
    }

    public String getBannar() {
        return bannar;
    }

    public void setBannar(String bannar) {
        this.bannar = bannar;
    }

    public String getBannarText() {
        return bannarText;
    }

    public void setBannarText(String bannarText) {
        this.bannarText = bannarText;
    }
}
