package com.soomter;

import com.google.gson.annotations.SerializedName;

public class MostWatchedCatgRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("UserId")
    public String UserId;
    @SerializedName("CategoryId")
    public int CategoryId;
    @SerializedName("SubCategoryId")
    public int SubCategoryId;
    @SerializedName("CityId")
    public int CityId;
    @SerializedName("AdsTo")
    public int AdsTo;
    @SerializedName("CompanyId")
    public int CompanyId;

    public MostWatchedCatgRequest(String lang , String UserId, int CategoryId,int SubCategoryId ,
                                  int CityId, int AdsTo ,int CompanyId) {
        this.lang = lang;
        this.UserId = UserId;
        this.CategoryId = CategoryId;
        this.SubCategoryId = SubCategoryId;
        this.CityId = CityId;
        this.AdsTo = AdsTo;
        this.CompanyId = CompanyId;
    }
}
