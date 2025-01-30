package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyProfileData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")

    public Result result = null;

    public class Result {
        @SerializedName("Id")
        public int Id;
        @SerializedName("CompanyBanchesCount")
        public int CompanyBanchesCount;
        @SerializedName("CompanyViewsCount")
        public int CompanyViewsCount;
        @SerializedName("CompanyAdsCount")
        public int CompanyAdsCount;
        @SerializedName("CompanyProductsCount")
        public int CompanyProductsCount;
        @SerializedName("CompanyEventsCount")
        public int CompanyEventsCount;
        @SerializedName("IsAppearInSite")
        public boolean IsAppearInSite;
        @SerializedName("ShowMobileInDetails")
        public boolean ShowMobileInDetails;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("CatgoryName")
        public String CatgoryName;
        @SerializedName("FieldName")
        public String FieldName;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("Trusted")
        public boolean Trusted;
        @SerializedName("Ma3rofUrl")
        public String Ma3rofUrl;
        @SerializedName("CountryName")
        public String CountryName;
        @SerializedName("Mobile")
        public String Mobile;
        @SerializedName("Address")
        public String Address;
        @SerializedName("MailBox")
        public String MailBox;
        @SerializedName("AdsEmail")
        public String AdsEmail;
        @SerializedName("Facebook")
        public String Facebook;
        @SerializedName("Twitter")
        public String Twitter;
        @SerializedName("Instagram")
        public String Instagram;
        @SerializedName("YouTube")
        public String YouTube;
        @SerializedName("linkedin")
        public String linkedin;
        @SerializedName("GooglePlus")
        public String GooglePlus;
        @SerializedName("Fax")
        public String Fax;
        @SerializedName("Phone")
        public String Phone;
        @SerializedName("CategoryId")
        public int CategoryId;
        @SerializedName("MessagesCount")
        public int MessagesCount;
        @SerializedName("CityName")
        public String CityName;
        @SerializedName("FieldId")
        public int FieldId;
        @SerializedName("UnifiedNumber")
        public String UnifiedNumber;
        @SerializedName("FreeNumber")
        public String FreeNumber;
        @SerializedName("Website")
        public String Website;
        @SerializedName("CommercialRegistrationNo")
        public String CommercialRegistrationNo;
        @SerializedName("Sunday_Thursday")
        public String Sunday_Thursday;
        @SerializedName("LogoId")
        public int LogoId;
        @SerializedName("RatingModel")
        public RatingModel ratingModel = null;
        @SerializedName("ListAds")
        public List<ListAds> listAds = null;
        @SerializedName("ListEvents")
        public List<ListEvents> listEvents = null;
        @SerializedName("SimilarCompanies")
        public List<SimilarCompanies> similarCompanies = null;
        @SerializedName("BranchsCompany")
        public List<BranchsCompany> branchsCompany = null;
        @SerializedName("TextAdRequests")
        public List<TextAdRequests> textAdRequests = null;
        @SerializedName("ListProducts")
        public List<ListProducts> listProducts = null;
    }

    public class RatingModel{
        @SerializedName("Id")
        public int Id;
        @SerializedName("TotalRaters")
        public int TotalRaters;
        @SerializedName("Rating")
        public int Rating;
        @SerializedName("AverageRating")
        public Double AverageRating;
        @SerializedName("RatingStarPercentage")
        public int RatingStarPercentage;

    }

    public class ListAds{
        @SerializedName("Id")
        public int Id;
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("Title")
        public String Title;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("ImageId")
        public int ImageId;
        @SerializedName("FromDate")
        public String FromDate;
        @SerializedName("ToDate")
        public String ToDate;
        @SerializedName("IsFavorite")
        public int IsFavorite;
        @SerializedName("IsPublished")
        public boolean IsPublished;
        @SerializedName("IsIdentified")
        public boolean IsIdentified;
        @SerializedName("IsIdentifieded")
        public int IsIdentifieded;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("Details")
        public String Details;
        @SerializedName("AdsRequestStatusId")
        public int AdsRequestStatusId;
        @SerializedName("RequestTypeId")
        public int RequestTypeId;
        @SerializedName("Status")
        public String Status;
        @SerializedName("Url")
        public String Url;
        @SerializedName("VedioUrl")
        public String VedioUrl;
        @SerializedName("IsVedioAds")
        public boolean IsVedioAds;
        @SerializedName("CompanyTitleKey")
        public String CompanyTitleKey;
        @SerializedName("MaxRows")
        public int MaxRows;
    }

    public class ListProducts{
        @SerializedName("Id")
        public int Id;
        @SerializedName("Title")
        public String Title;
        @SerializedName("Details")
        public String Details;
        @SerializedName("TradeMark")
        public String TradeMark;
        @SerializedName("PublishDate")
        public int PublishDate;
        @SerializedName("ImageId")
        public int ImageId;
        @SerializedName("Image")
        public String Image;
        @SerializedName("ExtraOffer")
        public int ExtraOffer;
        @SerializedName("Discount")
        public int Discount;
        @SerializedName("ProductTo")
        public int ProductTo;
        @SerializedName("Price")
        public String Price;
        @SerializedName("Rating")
        public int Rating;
        @SerializedName("TotalRaters")
        public int TotalRaters;
        @SerializedName("IdentificationEndDate")
        public String IdentificationEndDate;
        @SerializedName("IsFavorite")
        public int IsFavorite;
        @SerializedName("Rate")
        public int Rate;
        @SerializedName("ProductFieldValueId")
        public int ProductFieldValueId;
        @SerializedName("Filter")
        public int Filter;
        @SerializedName("SaleCount")
        public int SaleCount;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("CanOrder")
        public boolean CanOrder;
        @SerializedName("FromProflile")
        public int FromProflile;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("ProductTitleKey")
        public String ProductTitleKey;
        @SerializedName("MaxRows")
        public int MaxRows;
    }

    public class ListEvents{
        @SerializedName("Id")
        public int Id;
        @SerializedName("SeqNum")
        public int SeqNum;
        @SerializedName("Title")
        public String Title;
        @SerializedName("BusinessCategory")
        public String BusinessCategory;
        @SerializedName("CityName")
        public String CityName;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("EventType")
        public String EventType;
        @SerializedName("Details")
        public String Details;
        @SerializedName("ImageId")
        public int ImageId;
        @SerializedName("EventTypeId")
        public int EventTypeId;
        @SerializedName("IsStar")
        public boolean IsStar;
        @SerializedName("AgentCompanyId")
        public int AgentCompanyId;
        @SerializedName("IdentificationEndDate")
        public String IdentificationEndDate;
        @SerializedName("FromDate")
        public String FromDate;
        @SerializedName("FromTime")
        public String FromTime;
        @SerializedName("ToDate")
        public String ToDate;
        @SerializedName("ToTime")
        public String ToTime;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("CompanyId")
        public String CompanyId;
        @SerializedName("IsPublish")
        public boolean IsPublish;
        @SerializedName("IsFavorite")
        public int IsFavorite;
        @SerializedName("MaxRows")
        public int MaxRows;
        @SerializedName("CreateDate")
        public String CreateDate;
    }

    public class SimilarCompanies{
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("Summary")
        public String Summary;
        @SerializedName("LogoId")
        public int LogoId;
        @SerializedName("Rating")
        public int Rating;
        @SerializedName("TotalRaters")
        public int TotalRaters;
        @SerializedName("IsIdentified")
        public boolean IsIdentified;
    }

    public class BranchsCompany{
        @SerializedName("Id")
        public int Id;
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("BranchAddress")
        public String BranchAddress;
        @SerializedName("BranchPhone")
        public String BranchPhone;
        @SerializedName("BranchFax")
        public String BranchFax;
        @SerializedName("BranchMobile")
        public String BranchMobile;
        @SerializedName("Saturday")
        public String Saturday;
        @SerializedName("Sunday_Thursday")
        public String Sunday_Thursday;
        @SerializedName("Friday")
        public String Friday;
        @SerializedName("Latitude")
        public String Latitude;
        @SerializedName("Longitude")
        public String Longitude;
        @SerializedName("CountyId")
        public int CountyId;
        @SerializedName("CityName")
        public String CityName;
        @SerializedName("CityKey")
        public String CityKey;
        @SerializedName("FromDay")
        public String FromDay;
        @SerializedName("ToDay")
        public String ToDay;
        @SerializedName("FromTimeFirstSheft")
        public int FromTimeFirstSheft;
        @SerializedName("ToTimeFirstSheft")
        public int ToTimeFirstSheft;
        @SerializedName("FromTimesecondSheft")
        public int FromTimesecondSheft;
        @SerializedName("ToTimesecondSheft")
        public int ToTimesecondSheft;
        @SerializedName("BranchName")
        public String BranchName;
    }

    public class TextAdRequests{
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("Summary")
        public String Summary;
        @SerializedName("LogoId")
        public int LogoId;
        @SerializedName("Rating")
        public int Rating;
        @SerializedName("TotalRaters")
        public int TotalRaters;
        @SerializedName("IsIdentified")
        public boolean IsIdentified;
    }
}
