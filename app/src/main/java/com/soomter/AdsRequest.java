package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AdsRequest {

    @SerializedName("CategoryId")
    public Integer CategoryId;
    @SerializedName("Lang")
    public String lang;
    @SerializedName("Title")
    public String Title;
    @SerializedName("CityId")
    public int CityId;
    @SerializedName("AdsTo")
    public int AdsTo;
    @SerializedName("Count")
    public int Count;
    @SerializedName("Type")
    public int Type;


    public AdsRequest(Integer CategoryId, String lang, String title, int CityId, int AdsTo, int Count, int type) {
        this.CategoryId = CategoryId;
        this.lang = lang;
        this.Title = title;
        this.CityId = CityId;
        this.AdsTo = AdsTo;
        this.Count = Count;
        this.Type = type;
    }
}
