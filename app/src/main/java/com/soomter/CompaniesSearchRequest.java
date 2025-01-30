package com.soomter;

import com.google.gson.annotations.SerializedName;

public class CompaniesSearchRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("UserId")
    public int userId;
    @SerializedName("CategoryId")
    public int categoryId;
    @SerializedName("SubCategorId")
    public int subCategoryId;
    @SerializedName("CityId")
    public int cityId;
    @SerializedName("AdsTo")
    public int adsTo;
    @SerializedName("CurrentCompanyId")
    public int CurrentCompanyId;
    @SerializedName("PageSize")
    public int pageSize;
    @SerializedName("PageNumber")
    public int pageNumber;
    @SerializedName("SearchText")
    public String SearchText;


    public CompaniesSearchRequest(String lang, int userId, int categoryId, int subCategoryId, int cityId, int adsTo,
                                  int CurrentCompanyId, int pageSize , int pageNumber , String SearchText ) {
        this.lang = lang;
        this.userId = userId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.cityId = cityId;
        this.adsTo = adsTo;
        this.CurrentCompanyId = CurrentCompanyId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.SearchText = SearchText;
    }
}
