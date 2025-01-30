package com.soomter;

import com.google.gson.annotations.SerializedName;

public class ProfileReg {

    @SerializedName("FirstNameAr")
    public String firstNameAr;
    @SerializedName("FirstNameEn")
    public String firstNameEn;
    @SerializedName("LastNameAr")
    public String lastNameAr;
    @SerializedName("LastNameEn")
    public String lastNameEn;
    @SerializedName("Mobile")
    public String mobile;
    @SerializedName("Email")
    public String email;
    @SerializedName("Password")
    public String password;
    @SerializedName("Username")
    public String userName;
    @SerializedName("Lang")
    public String lang;

    public ProfileReg(String firstNameAr, String firstNameEn, String lastNameAr,
            String lastNameEn, String email, String mobile, String password,
                      String userName, String lang) {
        this.firstNameAr = firstNameAr;
        this.firstNameEn = firstNameEn;
        this.lastNameAr = lastNameAr;
        this.lastNameEn = lastNameEn;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.lang = lang;
        this.userName = userName;
    }
}
