/*
 * Created by Alvi Khalil on 11/2/18 4:18 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 4:18 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

public class BikeCard {
    private String title;
    private String image;
    private String description;
    private String price;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
