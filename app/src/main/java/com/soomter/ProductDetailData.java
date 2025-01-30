package com.soomter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailData {

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
        @SerializedName("FreeShipping")
        @Expose
        public boolean FreeShipping;
        @SerializedName("Id")
        @Expose
        public int Id;
        @SerializedName("IncludesValueAddedstring")
        @Expose
        public String IncludesValueAddedstring;
        @SerializedName("TradeMark")
        @Expose
        public String TradeMark;
        @SerializedName("Description")
        @Expose
        public String Description;
        @SerializedName("ImageId")
        @Expose
        public int ImageId;
        @SerializedName("Image")
        @Expose
        public String Image;
        @SerializedName("ShippingCityId")
        @Expose
        public String ShippingCityId;
        @SerializedName("Discount")
        @Expose
        public int Discount;
        @SerializedName("Price")
        @Expose
        public int Price;
        @SerializedName("PriceAfterDis")
        @Expose
        public int PriceAfterDis;
        @SerializedName("ProductTo")
        @Expose
        public int ProductTo;
        @SerializedName("IdentificationEndDate")
        @Expose
        public String IdentificationEndDate;
        @SerializedName("ShippingDate")
        @Expose
        public String ShippingDate;
        @SerializedName("CompanyId")
        @Expose
        public int CompanyId;
        @SerializedName("CompanyName")
        @Expose
        public String CompanyName;
        @SerializedName("Details")
        @Expose
        public String Details;
        @SerializedName("CatigoryId")
        @Expose
        public int CatigoryId;
        @SerializedName("ProductFieldValueId")
        @Expose
        public int ProductFieldValueId;
        @SerializedName("Title")
        @Expose
        public String Title;
        @SerializedName("TitleKey")
        @Expose
        public String TitleKey;
        @SerializedName("ProductsProperties")
        @Expose
        public List<ProductsProperties> productsProperties;
        @SerializedName("ImageList")
        @Expose
        public List<ImageList> imageLists;
        @SerializedName("ListSelectedProducts")
        @Expose
        public List<ListSelectedProducts> listSelectedProducts;
        @SerializedName("listRating")
        @Expose
        public List<ListRating> listRating;
    }

    public static class ProductsProperties {
        @SerializedName("Disabled")
        @Expose
        public boolean Disabled;
        @SerializedName("Group")
        @Expose
        public String Group;
        @SerializedName("Selected")
        @Expose
        public boolean Selected;
        @SerializedName("Text")
        @Expose
        public String Text;
        @SerializedName("Value")
        @Expose
        public String Value;
    }

    public static class ImageList {
        @SerializedName("Title")
        @Expose
        public String Title;
        @SerializedName("Order")
        @Expose
        public int Order;
        @SerializedName("Image")
        @Expose
        public String Image;
    }

    public static class ListSelectedProducts {
        @SerializedName("Id")
        @Expose
        public int Id;
        @SerializedName("Title")
        @Expose
        public String Title;
        @SerializedName("Details")
        @Expose
        public String Details;
        @SerializedName("TradeMark")
        @Expose
        public String TradeMark;
        @SerializedName("PublishDate")
        @Expose
        public String PublishDate;
        @SerializedName("ImageId")
        @Expose
        public int ImageId;
        @SerializedName("Image")
        @Expose
        public String Image;
        @SerializedName("ExtraOffer")
        @Expose
        public String ExtraOffer;
        @SerializedName("Discount")
        @Expose
        public int Discount;
        @SerializedName("ProductTo")
        @Expose
        public int ProductTo;
        @SerializedName("Price")
        @Expose
        public int Price;
        @SerializedName("Rating")
        @Expose
        public Double Rating;
        @SerializedName("TotalRaters")
        @Expose
        public int TotalRaters;
        @SerializedName("IdentificationEndDate")
        @Expose
        public String IdentificationEndDate;
        @SerializedName("IsFavorite")
        @Expose
        public int IsFavorite;
        @SerializedName("Rate")
        @Expose
        public int Rate;
        @SerializedName("ProductFieldValueId")
        @Expose
        public int ProductFieldValueId;
        @SerializedName("Filter")
        @Expose
        public int Filter;
        @SerializedName("SaleCount")
        @Expose
        public int SaleCount;
        @SerializedName("CompanyName")
        @Expose
        public String CompanyName;
        @SerializedName("CompanyId")
        @Expose
        public int CompanyId;
        @SerializedName("CanOrder")
        @Expose
        public boolean CanOrder;
        @SerializedName("FromProflile")
        @Expose
        public int FromProflile;
        @SerializedName("TitleKey")
        @Expose
        public String TitleKey;

        @SerializedName("ProductTitleKey")
        @Expose
        public String ProductTitleKey;
        @SerializedName("MaxRows")
        @Expose
        public int MaxRows;
    }

    public static class ListRating {
        @SerializedName("Id")
        @Expose
        public int Id;
        @SerializedName("Name")
        @Expose
        public String Name;
        @SerializedName("RatingDate")
        @Expose
        public String RatingDate;
        @SerializedName("RateText")
        @Expose
        public String RateText;
        @SerializedName("ProductId")
        @Expose
        public int ProductId;
        @SerializedName("UserId")
        @Expose
        public String UserId;
        @SerializedName("CompanyId")
        @Expose
        public int CompanyId;
        @SerializedName("TitleKey")
        @Expose
        public String TitleKey;
        @SerializedName("Rating")
        @Expose
        public Float Rating;
        @SerializedName("TotalRaters")
        @Expose
        public int TotalRaters;
        @SerializedName("Comment")
        @Expose
        public String Comment;
        @SerializedName("Rate")
        @Expose
        public String Rate;
        @SerializedName("Advantages")
        @Expose
        public String Advantages;
        @SerializedName("DisAdvantages")
        @Expose
        public String DisAdvantages;
        @SerializedName("Title")
        @Expose
        public String Title;
        @SerializedName("MaxRows")
        @Expose
        public int MaxRows;
        @SerializedName("listRating")
        @Expose
        public String listRating;
        @SerializedName("RatingModel")
        @Expose
        public String RatingModel;

    }
}
