package com.soomter;

import com.google.gson.annotations.SerializedName;

public class ProductFavRequest {

    @SerializedName("ProductFieldValueId")
    public int ProductFieldValueId;
    @SerializedName("ProductId")
    public int ProductId;
    @SerializedName("CompanyId")
    public int CompanyId;
    @SerializedName("UserId")
    public String UserId;

    public ProductFavRequest(String UserId, int ProductId, int CompanyId, int ProductFieldValueId) {
        this.ProductFieldValueId = ProductFieldValueId;
        this.ProductId = ProductId;
        this.CompanyId = CompanyId;
        this.UserId = UserId;
    }
}
