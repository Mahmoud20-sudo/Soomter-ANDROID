package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.soomter.utils.ListInterface;

public class BestSellingData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public List<Datum> result = null;

    public class Datum implements ListInterface {
        @SerializedName("Id")
        public int id;
        @SerializedName("Title")
        public String Title;
        @SerializedName("Details")
        public String Details;
        @SerializedName("PublishDate")
        public String PublishDate;
        @SerializedName("ImageId")
        public int ImageId;
        @SerializedName("Image")
        public String Image;
        @SerializedName("ExtraOffer")
        public String ExtraOffer;
        @SerializedName("Discount")
        public String Discount;
        @SerializedName("ProductTo")
        public String ProductTo;
        @SerializedName("Price")
        public int Price;
        @SerializedName("Rating")
        public int Rating;
        @SerializedName("TotalRaters")
        public int TotalRaters;
        @SerializedName("IdentificationEndDate")
        public String IdentificationEndDate;
        @SerializedName("IsFavorite")
        public int IsFavorite;
        @SerializedName("Rate")
        public int Rate;
        @SerializedName("ProductFieldValueId")
        public int ProductFieldValueId;
        @SerializedName("Filter")
        public int Filter;
        @SerializedName("SaleCount")
        public int SaleCount;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("CanOrder")
        public boolean CanOrder;
        @SerializedName("FromProflile")
        public int FromProflile;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("ProductTitleKey")
        public String ProductTitleKey;
        @SerializedName("MaxRows")
        public int MaxRows;
    }
}
