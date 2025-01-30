package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AdsStatisticsRequest {

    @SerializedName("AdsTo")
    public Integer 	AdsTo;

    public AdsStatisticsRequest(Integer AdsTo) {
        this.AdsTo = AdsTo;
    }
}
