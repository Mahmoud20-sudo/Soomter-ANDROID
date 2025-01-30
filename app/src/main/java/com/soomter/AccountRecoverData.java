package com.soomter;

import com.google.gson.annotations.SerializedName;

public class AccountRecoverData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Datum result = null;

    public class Datum {
        @SerializedName("Result")
        public Result result = null;
        @SerializedName("IsSuccess")
        public boolean IsSuccess;
        @SerializedName("Message")
        public String Message;
    }

    public class Result {
        @SerializedName("Type")
        public int Type;
        @SerializedName("Email")
        public String Email;
        @SerializedName("Phone")
        public String Phone;
        @SerializedName("Method")
        public int Method;
    }
}
