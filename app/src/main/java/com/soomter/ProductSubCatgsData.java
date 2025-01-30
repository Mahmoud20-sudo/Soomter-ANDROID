package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductSubCatgsData {

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
        @SerializedName("Image")
        public String image;
        public boolean isChecked;
    }
}
