package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AccountRecover {

    @SerializedName("Type")
    public int Type;
    @SerializedName("Email")
    public String Email;
    @SerializedName("Method")
    public int Method;


    public AccountRecover(String Email,int Method , int Type) {
        this.Email = Email;
        this.Method = Method;
        this.Type = Type;
    }
}
