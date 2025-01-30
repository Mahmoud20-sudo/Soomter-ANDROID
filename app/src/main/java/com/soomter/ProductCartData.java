package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCartData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Result result = null;

    public class Result {
        @SerializedName("Items")
        public List<Items> Items;
        @SerializedName("ShippingInfo")
        public ShippingInfo ShippingInfo;
        @SerializedName("ShippingCity")
        public String ShippingCity;
        @SerializedName("DeliverDate")
        public String DeliverDate;
        @SerializedName("CashOnDeliveryFees")
        public int CashOnDeliveryFees;
        @SerializedName("ShippingExpenses")
        public int ShippingExpenses;
    }

    public class ShippingInfo {
        @SerializedName("Id")
        public int Id;
        @SerializedName("UserId")
        public String UserId;
        @SerializedName("ReceiverName")
        public String ReceiverName;
        @SerializedName("Address")
        public String Address;
        @SerializedName("ContactTel")
        public int ContactTel;
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

    public class Items {
        @SerializedName("QuantityList")
        public List<QuantityList> QuantityList;
        @SerializedName("FreeShipping")
        public boolean FreeShipping;
        @SerializedName("IncludesVat")
        public boolean IncludesVat;
        @SerializedName("IsProductAvailable")
        public boolean IsProductAvailable;
        @SerializedName("CashOnDeliveryFees")
        public int CashOnDeliveryFees;
        @SerializedName("Id")
        public int Id;
        @SerializedName("ProductId")
        public int ProductId;
        @SerializedName("ProductFieldValueId")
        public int ProductFieldValueId;
        @SerializedName("Title")
        public String Title;
        @SerializedName("CompanyId")
        public int CompanyId;
        @SerializedName("ImageId")
        public int ImageId;
        @SerializedName("ProductImageId")
        public int ProductImageId;
        @SerializedName("Details")
        public String Details;
        @SerializedName("TradeMark")
        public String TradeMark;
        @SerializedName("CompanyName")
        public String CompanyName;
        @SerializedName("ItemQuantity")
        public int ItemQuantity;
        @SerializedName("TitleKey")
        public String TitleKey;
        @SerializedName("ProductTitleKey")
        public String ProductTitleKey;
        @SerializedName("PriceWithoutDiscount")
        public int PriceWithoutDiscount;
        @SerializedName("Price")
        public Double Price;
        @SerializedName("Discount")
        public int Discount;
        @SerializedName("VatValue")
        public Double VatValue;
        @SerializedName("FieldValues")
        public String FieldValues;
        @SerializedName("FieldValueParentId")
        public int FieldValueParentId;

    }
    public class QuantityList {

        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("IdString")
        public String IdString;
    }
}
