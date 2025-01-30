package com.soomter;

import com.google.gson.annotations.SerializedName;

public class CompanyReg {

    @SerializedName("TitleAr")
    public String titleAr;
    @SerializedName("TitleEn")
    public String titleEn;
    @SerializedName("Mobile")
    public String mobile;
    @SerializedName("Email")
    public String Email;
    @SerializedName("Password")
    public String password;
    @SerializedName("Username")
    public String userName;
    @SerializedName("Lang")
    public String lang;



    public CompanyReg(String titleAr, String titleEn, String email, String mobile, String password,
                      String userName, String lang) {
        this.titleEn = titleEn;
        this.titleAr = titleAr;
        this.mobile = mobile;
        this.Email = email;
        this.password = password;
        this.lang = lang;
        this.userName = userName;
    }
}
