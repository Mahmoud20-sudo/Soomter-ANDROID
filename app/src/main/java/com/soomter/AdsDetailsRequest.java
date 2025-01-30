package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AdsDetailsRequest {

    @SerializedName("Id")
    public Integer Id;
    @SerializedName("Lang")
    public String lang;
    @SerializedName("CurrentUserId")
    public int CurrentUserId;
    @SerializedName("Type")
    public int Type;

    public AdsDetailsRequest(Integer Id, String lang, int CurrentUserId, int type) {
        this.Id = Id;
        this.lang = lang;
        this.CurrentUserId = CurrentUserId;
        this.Type = type;
    }
}
