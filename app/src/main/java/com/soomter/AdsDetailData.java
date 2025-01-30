package com.soomter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdsDetailData {

    @SerializedName("Status")
    @Expose
    public Integer status;
    @SerializedName("Error")
    @Expose
    public Integer error;
    @SerializedName("Result")
    @Expose
    public Datum result = null;

    public static class Datum {
        @SerializedName("Id")
        @Expose
        public int Id;
        @SerializedName("CompanyName")
        @Expose
        public String CompanyName;
        @SerializedName("EnCompanyName")
        @Expose
        public String EnCompanyName;
        @SerializedName("CityName")
        @Expose
        public String CityName;
        @SerializedName("FromDate")
        @Expose
        public String FromDate;
        @SerializedName("ToDate")
        @Expose
        public String ToDate;
        @SerializedName("ImageId")
        @Expose
        public int ImageId;
        @SerializedName("Image")
        @Expose
        public String Image;
        @SerializedName("Details")
        @Expose
        public String Details;
        @SerializedName("CategoryId")
        @Expose
        public int CategoryId;
        @SerializedName("Url")
        @Expose
        public String Url;
        @SerializedName("Title")
        @Expose
        public String Title;
        @SerializedName("IsFavorite")
        @Expose
        public int IsFavorite;
        @SerializedName("CategoryName")
        @Expose
        public String CategoryName;
        @SerializedName("Phone")
        @Expose
        public String Phone;
        @SerializedName("PhoneKey")
        @Expose
        public String PhoneKey;
        @SerializedName("Mobile")
        @Expose
        public String Mobile;
        @SerializedName("SubCategoryName")
        @Expose
        public String SubCategoryName;
        @SerializedName("CompanyId")
        @Expose
        public int CompanyId;
        @SerializedName("CurrentCompanyId")
        @Expose
        public int CurrentCompanyId;
        @SerializedName("AdsCatigoryId")
        @Expose
        public int AdsCatigoryId;
        @SerializedName("AdsCatigoryName")
        @Expose
        public String AdsCatigoryName;
        @SerializedName("TitleKey")
        @Expose
        public String TitleKey;
        @SerializedName("AdsTo")
        @Expose
        public int AdsTo;
        @SerializedName("ListSemilarAds")
        @Expose
        public List<SimilarAds> ListSemilarAds;
        @SerializedName("IsIdentified")
        @Expose
        public boolean IsIdentified;
        @SerializedName("IsVedioAds")
        @Expose
        public boolean IsVedioAds;
        @SerializedName("VedioUrl")
        @Expose
        public String VedioUrl;
        @SerializedName("CityList")
        @Expose
        public String CityList;
        @SerializedName("ListBusinessCategoriesViewModel")
        @Expose
        public String ListBusinessCategoriesViewModel;

        public static class SimilarAds {
            @SerializedName("Id")
            @Expose
            public int Id;
            @SerializedName("CompanyId")
            @Expose
            public int CompanyId;
            @SerializedName("Title")
            @Expose
            public String Title;
            @SerializedName("CompanyName")
            @Expose
            public String CompanyName;
            @SerializedName("ImageId")
            @Expose
            public int ImageId;
            @SerializedName("FromDate")
            @Expose
            public String FromDate;
            @SerializedName("ToDate")
            @Expose
            public String ToDate;
            @SerializedName("IsFavorite")
            @Expose
            public int IsFavorite;
            @SerializedName("IsPublished")
            @Expose
            public boolean IsPublished;
            @SerializedName("IsIdentified")
            @Expose
            public boolean IsIdentified;
            @SerializedName("TitleKey")
            @Expose
            public String TitleKey;
            @SerializedName("Details")
            @Expose
            public String Details;
            @SerializedName("AdsRequestStatusId")
            @Expose
            public int AdsRequestStatusId;
            @SerializedName("AdsRequestStatus")
            @Expose
            public String AdsRequestStatus;
            @SerializedName("Url")
            @Expose
            public String Url;
            @SerializedName("VedioUrl")
            @Expose
            public String VedioUrl;
            @SerializedName("IsVedioAds")
            @Expose
            public boolean IsVedioAds;
            @SerializedName("CompanyTitleKey")
            @Expose
            public String CompanyTitleKey;
            @SerializedName("MaxRows")
            @Expose
            public int MaxRows;
        }

    }
}
