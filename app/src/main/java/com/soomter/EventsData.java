package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Datum result = null;


    public static class Datum {
        @SerializedName("TotalItemsCount")
        public Integer TotalItemsCount;
        @SerializedName("Items")
        public List<Items> Items;
    }

    public static class Items{
        @SerializedName("Id")
        public Integer Id;
        @SerializedName("Period")
        public String Period;
        @SerializedName("Image")
        public String Image;
        @SerializedName("Title")
        public String Title;
        @SerializedName("Address")
        public String Address;
        @SerializedName("IsStar")
        public boolean IsStar;
        @SerializedName("IsFavorite")
        public int IsFavorite;

    }
}
