package com.soomter;

import com.google.gson.annotations.SerializedName;

public class ProdsSearchRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("Title")
    public String Title;
    @SerializedName("CategoryId")
    public int CategoryId;
    @SerializedName("ProductTo")
    public int ProductTo;
    @SerializedName("MinPrice")
    public int MinPrice;
    @SerializedName("MaxPrice")
    public int MaxPrice;
    @SerializedName("CurrentUserId")
    public String CurrentUserId;
    @SerializedName("IsFavourit")
    public int IsFavourit;
    @SerializedName("TradeMarkIds")
    public String TradeMarkIds;

    public ProdsSearchRequest(String lang, String Title, int CategoryId, int ProductTo, int MinPrice, int MaxPrice
                                , String CurrentUserId , int IsFavourit , String TradeMarkIds) {
        this.lang = lang;
        this.Title = Title;
        this.CategoryId = CategoryId;
        this.ProductTo = ProductTo;
        this.MinPrice = MinPrice;
        this.MaxPrice = MaxPrice;
        this.CurrentUserId = CurrentUserId;
        this.IsFavourit = IsFavourit;
        this.TradeMarkIds = TradeMarkIds;
    }
}
