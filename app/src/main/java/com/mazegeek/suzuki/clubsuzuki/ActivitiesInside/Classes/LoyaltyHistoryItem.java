package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes;

import java.io.Serializable;

public class LoyaltyHistoryItem implements Serializable {

    private String shopName;
    private String offerCode;
    private String offerName;
    private String salesPhone;
    private String usageTime;
    private String userQuota;
    private String usageNumber;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public void setSalesPhone(String salesPhone) {
        this.salesPhone = salesPhone;
    }

    public String getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(String usageTime) {
        this.usageTime = usageTime;
    }

    public String getUserQuota() {
        return userQuota;
    }

    public void setUserQuota(String userQuota) {
        this.userQuota = userQuota;
    }

    public String getUsageNumber() {
        return usageNumber;
    }

    public void setUsageNumber(String usageNumber) {
        this.usageNumber = usageNumber;
    }
}
