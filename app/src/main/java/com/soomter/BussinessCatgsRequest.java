package com.soomter;

import com.google.gson.annotations.SerializedName;

public class BussinessCatgsRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("BusinessCategoryId")
    public int BusinessCategoryId;


    public BussinessCatgsRequest(String lang , int BusinessCategoryId) {
        this.lang = lang;
        this.BusinessCategoryId = BusinessCategoryId;
    }
}
