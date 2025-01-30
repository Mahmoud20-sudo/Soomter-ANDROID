package com.soomter;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    @SerializedName("Status")
    public Integer status;
    @SerializedName("Error")
    public Integer error;
    @SerializedName("Result")
    public Datum result = null;

    public class Datum {

        @SerializedName("Id")
        public String id;
        @SerializedName("Name")
        public String name;
        @SerializedName("Email")
        public String email;
        @SerializedName("UserType")
        public String userType;
        @SerializedName("RelatedId")
        public Integer relatedId;

    }
}
