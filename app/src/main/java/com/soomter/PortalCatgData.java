package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.soomter.utils.ListInterface;

public class PortalCatgData {

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
        public String name;
        @SerializedName("EnglishName")
        public String nameEN;
        @SerializedName("BusinessIcon64")
        public String businessIcon64;
        @SerializedName("BusinessImage64")
        public String businessImage64;
        @SerializedName("BusinessIconId ")
        public int businessIconId ;
        @SerializedName("BusinessImageId")
        public int businessImageId;
    }
}
