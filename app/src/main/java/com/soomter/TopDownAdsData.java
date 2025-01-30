package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopDownAdsData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Result result = null;

    public static class Result {
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("Url")
        public String Url;
        @SerializedName("Image")
        public String Image;
    }

}
