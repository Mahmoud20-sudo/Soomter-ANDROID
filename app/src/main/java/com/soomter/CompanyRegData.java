package com.soomter;

import com.google.gson.annotations.SerializedName;

public class CompanyRegData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Datum result = null;

    public class Datum {

        @SerializedName("UserId")
        public String userID;
        @SerializedName("Message")
        public String message;
    }
}
