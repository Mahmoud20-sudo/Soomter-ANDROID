package com.soomter;

import com.google.gson.annotations.SerializedName;

public class GeneralCompRequest {

    @SerializedName("CompanyId")
    public int Id;
    @SerializedName("Name")
    public String Name;
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("Email")
    public String Email;
    @SerializedName("RequestDetails")
    public String RequestDetails;
    @SerializedName("UserId")
    public String UserId;

    public GeneralCompRequest(Integer Id, String Name,String Email,  String Mobile, String RequestDetails ,String UserId) {
        this.Id = Id;
        this.Name = Name;
        this.Mobile = Mobile;
        this.Email = Email;
        this.RequestDetails = RequestDetails;
        this.UserId = UserId;
    }
}
