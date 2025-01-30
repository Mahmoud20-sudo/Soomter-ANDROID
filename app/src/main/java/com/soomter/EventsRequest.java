package com.soomter;

import com.google.gson.annotations.SerializedName;

public class EventsRequest {

    @SerializedName("ComapnyId")
    public int ComapnyId;
    @SerializedName("Lang")
    public String lang;
    @SerializedName("UserId")
    public String UserId;
    @SerializedName("CategoryId")
    public int CategoryId;
    @SerializedName("EventTypeId")
    public int EventTypeId;
    @SerializedName("CityId")
    public int CityId;
    @SerializedName("FromDate")
    public String FromDate;
    @SerializedName("ToDate")
    public String ToDate;
    @SerializedName("SearchText")
    public String SearchText;
    @SerializedName("PageSize")
    public int PageSize;
    @SerializedName("PageNumber")
    public int PageNumber;
    @SerializedName("IsFavourit")
    public int IsFavourit;

    public EventsRequest(int ComapnyId, String lang, String UserId, int CategoryId, int EventTypeId
            , int CityId, String FromDate, String ToDate, int PageSize, int PageNumber , String SearchText) {
        this.CategoryId = CategoryId;
        this.lang = lang;
        this.ComapnyId = ComapnyId;
        this.UserId = UserId;
        this.EventTypeId = EventTypeId;
        this.CityId = CityId;
        this.FromDate = FromDate;
        this.ToDate = ToDate;
        this.SearchText = SearchText;
        this.PageNumber = PageNumber;
        this.PageSize = PageSize;
    }

    public EventsRequest(int ComapnyId, String lang, String UserId, int CategoryId, int EventTypeId
            , int CityId, String FromDate, String ToDate, int PageSize, int PageNumber , String SearchText, int IsFavourit) {
        this.CategoryId = CategoryId;
        this.lang = lang;
        this.ComapnyId = ComapnyId;
        this.UserId = UserId;
        this.EventTypeId = EventTypeId;
        this.CityId = CityId;
        this.FromDate = FromDate;
        this.ToDate = ToDate;
        this.SearchText = SearchText;
        this.PageNumber = PageNumber;
        this.PageSize = PageSize;
        this.IsFavourit = IsFavourit;
    }
}
