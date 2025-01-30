package com.soomter;

import com.google.gson.annotations.SerializedName;

public class PortalCatgRequest {

    @SerializedName("Lang")
    public String lang;


    public PortalCatgRequest(String lang) {
        this.lang = lang;
    }
}
