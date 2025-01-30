package com.soomter;

import com.google.gson.annotations.SerializedName;

public class CitiesRequest {

    @SerializedName("CountryId")
    public Integer countryID;
    @SerializedName("Lang")
    public String lang;


    public CitiesRequest(Integer countryID, String lang) {
        this.countryID = countryID;
        this.lang = lang;
    }
}
