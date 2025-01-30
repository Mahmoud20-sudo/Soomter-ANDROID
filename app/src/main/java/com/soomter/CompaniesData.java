package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompaniesData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")

    public Result result = null;

    public class Result {

        @SerializedName("TotalItemsCount")
        public int totalItemsCount;
        public List<Items> Items = null;

    }

    public static class Items{
        @SerializedName("Id")
        public int Id;
        @SerializedName("IsAdminAdd")
        public String IsAdminAdd;
        @SerializedName("LogoId")
        public int LogoId;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("CompanyEmail")
        public String CompanyEmail;
        @SerializedName("Website")
        public String Website;
        @SerializedName("Summary")
        public String Summary;
        @SerializedName("Phone")
        public String Phone;
        @SerializedName("PostNumber")
        public int PostNumber;
        @SerializedName("MailBox")
        public String MailBox;
        @SerializedName("Ma3rofUrl")
        public String Ma3rofUrl;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("CountEvents")
        public int CountEvents;
        @SerializedName("CountProduct")
        public int CountProduct;
        @SerializedName("ViewCounts")
        public int ViewCounts;
        @SerializedName("CountADS")
        public int CountADS;
        @SerializedName("CountryName")
        public String CountryName;
        @SerializedName("CityName")
        public String CityName;
        @SerializedName("Key")
        public String Key;
        @SerializedName("Address")
        public String Address;
        @SerializedName("FeaturedCompany")
        public boolean FeaturedCompany;
        @SerializedName("Trusted")
        public boolean Trusted;
        @SerializedName("FieldId")
        public int FieldId;
        @SerializedName("IsFlowed")
        public int IsFlowed;
        @SerializedName("TotalRaters")
        public int TotalRaters;
        @SerializedName("Advertising")
        public String Advertising;
        @SerializedName("AverageRating")
        public int AverageRating;
        @SerializedName("companyLogo")
        public String companyLogo;
        @SerializedName("FreeNumber")
        public String FreeNumber;
        @SerializedName("EndHighlightDate")
        public String EndHighlightDate;
        @SerializedName("CompanyUserId")
        public String CompanyUserId;
        @SerializedName("LastLoginDate")
        public String LastLoginDate;
        @SerializedName("AdvertisingStartDate")
        public String AdvertisingStartDate;
        @SerializedName("AdvertisingEndDate")
        public String AdvertisingEndDate;
        @SerializedName("IsIdentefied")
        public boolean IsIdentefied;
        @SerializedName("MaxRows")
        public int MaxRows;
    }
}
