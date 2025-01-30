package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsStatisticsData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Datum result = null;

    public static class Datum {
        @SerializedName("ImageAdsCount")
        public int ImageAdsCount;
        @SerializedName("VedioAdsCount")
        public int VedioAdsCount;
        @SerializedName("StarAdsCount")
        public int StarAdsCount;
        @SerializedName("TextAdsCount")
        public int TextAdsCount;
        @SerializedName("AllAds")
        public int AllAds;
    }
}
