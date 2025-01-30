package com.soomter;

import com.google.gson.annotations.SerializedName;

public class UserLogin {

    @SerializedName("Username")
    public String username;
    @SerializedName("Password")
    public String password;


    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
