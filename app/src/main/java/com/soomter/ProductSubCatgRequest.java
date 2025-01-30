package com.soomter;

import com.google.gson.annotations.SerializedName;

public class ProductSubCatgRequest {

    @SerializedName("CategoryId")
    public Integer categoryId;
    @SerializedName("Lang")
    public String lang;


    public ProductSubCatgRequest(Integer categoryId, String lang) {
        this.categoryId = categoryId;
        this.lang = lang;
    }
}
