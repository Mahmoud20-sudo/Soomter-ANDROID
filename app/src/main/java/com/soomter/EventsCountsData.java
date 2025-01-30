package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsCountsData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public List<Datum> result = null;

    public static class Datum {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("Count")
        public int Count;
    }
}
