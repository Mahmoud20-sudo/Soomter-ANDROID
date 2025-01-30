package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.soomter.utils.ListInterface;

public class ProductCatgsTreeData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public List<Datum> result = null;

    public class Datum {

        @SerializedName("Id")
        public int id;
        @SerializedName("Name")
        public String name;
        @SerializedName("Image")
        public String Image;
        @SerializedName("Childs")
        public List<Childs> childs = null;
    }

    public class Childs {

        @SerializedName("Id")
        public int id;
        @SerializedName("Name")
        public String name;
        @SerializedName("Image")
        public String Image;
    }
}
