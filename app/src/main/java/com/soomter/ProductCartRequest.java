package com.soomter;

import com.google.gson.annotations.SerializedName;

public class ProductCartRequest {

    @SerializedName("ProductFieldValueId")
    public int ProductFieldValueId;
    @SerializedName("ProductId")
    public int ProductId;
    @SerializedName("Quantitiy")
    public int Quantitiy;
    @SerializedName("UserId")
    public String UserId;

    public ProductCartRequest(String UserId, int ProductId, int Quantitiy, int ProductFieldValueId) {
        this.ProductFieldValueId = ProductFieldValueId;
        this.ProductId = ProductId;
        this.Quantitiy = Quantitiy;
        this.UserId = UserId;
    }
}
