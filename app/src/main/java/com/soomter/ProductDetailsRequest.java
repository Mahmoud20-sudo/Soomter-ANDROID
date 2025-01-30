package com.soomter;

import com.google.gson.annotations.SerializedName;

public class ProductDetailsRequest {

    @SerializedName("ProductId")
    public Integer ProductId;
    @SerializedName("FieldId")
    public Integer FieldId;
    @SerializedName("Lang")
    public String lang;
    @SerializedName("CurrentUserId")
    public String CurrentUserId;


    public ProductDetailsRequest(Integer ProductId, Integer FieldId, String CurrentUserId, String lang) {
        this.ProductId = ProductId;
        this.FieldId = FieldId;
        this.CurrentUserId = CurrentUserId;
        this.lang = lang;
    }
}
