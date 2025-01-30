package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.soomter.utils.ListInterface;

public class ProductsCatgData {

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
        @SerializedName("Image")
        public String Image;
    }
}
