/*
 * Created by Alvi Khalil on 11/2/18 3:27 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 2:51 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

public class ClubItems {
    private String url;
    private String Image;
    private String header;
    private String body;
    private Integer testImage;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getTestImage() {
        return testImage;
    }

    public void setTestImage(Integer testImage) {
        this.testImage = testImage;
    }
}
