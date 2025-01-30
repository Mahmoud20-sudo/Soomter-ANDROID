package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Datum result = null;

    public static class ImagesAds {
        @SerializedName("Id")
        public int id;
        @SerializedName("Title")
        public String Title;
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("VedioUrl")
        public String VedioUrl;
        @SerializedName("IsIdentified")
        public int IsIdentified;
        @SerializedName("Type")
        public int Type;
        @SerializedName("ImageId")
        public int ImageId;
        @SerializedName("Image")
        public String Image;
    }

    public static class Datum {
        @SerializedName("PromotedAds")
        public List<ImagesAds> promAds = null;
        @SerializedName("ImageAds")
        public List<ImagesAds> imgsAds = null;
        @SerializedName("VideoAds")
        public List<ImagesAds> videoAds = null;
        @SerializedName("TextAds")
        public List<ImagesAds> textAds = null;
    }
}
