package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductFavData {
    @SerializedName("Status")
    public int status;
    @SerializedName("Error")
    public String error;
    @SerializedName("Result")
    public boolean result;
}
