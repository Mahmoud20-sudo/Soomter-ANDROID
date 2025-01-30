package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.soomter.utils.ListInterface;

public class PromotedProductsData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public List<Datum> result = null;

    public class Datum implements ListInterface {
        @SerializedName("Id")
        public int id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("Details")
        public String Details;
        @SerializedName("IconID")
        public int IconID;
        @SerializedName("IsPublish")
        public boolean IsPublish;
        @SerializedName("IsActive")
        public boolean IsActive;
        @SerializedName("Order")
        public boolean Order;
        @SerializedName("Image")
        public String Image;
    }
}
