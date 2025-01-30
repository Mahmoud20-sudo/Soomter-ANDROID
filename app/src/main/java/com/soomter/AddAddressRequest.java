package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AddAddressRequest {

    @SerializedName("UserId")
    public String UserId;
    @SerializedName("ReceiverName")
    public String ReceiverName;
    @SerializedName("Address")
    public String Address;
    @SerializedName("ContactTel")
    public String ContactTel;
    @SerializedName("CityId")
    public int CityId;
    @SerializedName("IsDefault")
    public boolean IsDefault;
    @SerializedName("Region")
    public String Region;
    @SerializedName("StreetNo")
    public String StreetNo;
    @SerializedName("FloorNo")
    public String FloorNo;
    @SerializedName("Trademark")
    public String Trademark;


    public AddAddressRequest(String UserId, String ReceiverName, String ContactTel, String Address) {
        this.UserId = UserId;
        this.ReceiverName = ReceiverName;
        this.ContactTel = ContactTel;
        this.Address = Address;
    }

}
