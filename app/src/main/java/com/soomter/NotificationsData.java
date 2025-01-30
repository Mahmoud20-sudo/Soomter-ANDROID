package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsData {

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
        @SerializedName("Title")
        public String Title;
        @SerializedName("Image")
        public String Image;
        @SerializedName("MessageBody")
        public String MessageBody;
        @SerializedName("ShortMessageBody")
        public String ShortMessageBody;
        @SerializedName("CreateDate")
        public String CreateDate;
    }
}
