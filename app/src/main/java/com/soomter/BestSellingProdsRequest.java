package com.soomter;

import com.google.gson.annotations.SerializedName;

public class BestSellingProdsRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("Count")
    public int count;

    public BestSellingProdsRequest(String lang, int count) {
        this.lang = lang;
        this.count = count;
    }
}
