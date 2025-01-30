package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BussinesCatgData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public List<Datum> result = null;

    public static class Datum {

        @SerializedName("Id")
        public int id;
        @SerializedName("Name")
        public String name;
        public boolean isChecked;
    }
}
