package com.soomter;

import com.google.gson.annotations.SerializedName;

public class CompaniesRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("UserId")
    public int userId;
    @SerializedName("CategoryId")
    public int categoryId;
    @SerializedName("SubCategoryId")
    public int subCategoryId;
    @SerializedName("CityId")
    public int cityId;
    @SerializedName("AdsTo")
    public int adsTo;
    @SerializedName("CompanyId")
    public int companyId;
    @SerializedName("PageSize")
    public int pageSize;
    @SerializedName("PageNumber")
    public int pageNumber;


    public CompaniesRequest(String lang, int userId, int categoryId, int subCategoryId, int cityId, int adsTo, int companyId
                            ,int pageSize , int pageNumber) {
        this.lang = lang;
        this.userId = userId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.cityId = cityId;
        this.adsTo = adsTo;
        this.companyId = companyId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
