package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductAddreseData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public List<Result> result = null;

    public class Result {
        @SerializedName("Id")
        public int Id;
        @SerializedName("UserId")
        public String UserId;
        @SerializedName("ReceiverName")
        public String ReceiverName;
        @SerializedName("Address")
        public String Address;
        @SerializedName("ContactTel")
        public String ContactTel;
        @SerializedName("Isdefault")
        public boolean Isdefault;
        @SerializedName("IsDeleted")
        public boolean IsDeleted;
        @SerializedName("CityId")
        public int CityId;
        @SerializedName("City")
        public String City;
        @SerializedName("Region")
        public String Region;
        @SerializedName("StreetNo")
        public String StreetNo;
        @SerializedName("FloorNo")
        public String FloorNo;
        @SerializedName("Trademark")
        public String Trademark;
    }


}
