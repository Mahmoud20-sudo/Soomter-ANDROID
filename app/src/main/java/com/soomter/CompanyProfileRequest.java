package com.soomter;

import com.google.gson.annotations.SerializedName;

public class CompanyProfileRequest {

    @SerializedName("Lang")
    public String lang;
    @SerializedName("UserId")
    public int UserId;
    @SerializedName("Id")
    public int Id;


    public CompanyProfileRequest(String lang, int userId, int Id) {
        this.lang = lang;
        this.UserId = userId;
        this.Id = Id;
    }
}