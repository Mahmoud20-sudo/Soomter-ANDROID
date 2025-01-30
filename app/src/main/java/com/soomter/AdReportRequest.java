package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AdReportRequest {

    @SerializedName("Name")
    public String Name;
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("Email")
    public String Email;
    @SerializedName("UserId")
    public String UserId;
    @SerializedName("Title")
    public String Title;
    @SerializedName("Body")
    public String Body;
    @SerializedName("AdsId")
    public int AdsId;
    @SerializedName("CompanyId")
    public int CompanyId;

    public AdReportRequest(String Name, String Mobile, String Email, String UserId, String Title
            , String Body, int AdsId, int CompanyId) {
        this.Name = Name;
        this.Mobile = Mobile;
        this.Email = Email;
        this.UserId = UserId;
        this.Title = Title;
        this.Body = Body;
        this.AdsId = AdsId;
        this.CompanyId = CompanyId;
    }
}
