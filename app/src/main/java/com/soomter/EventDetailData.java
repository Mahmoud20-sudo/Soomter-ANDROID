package com.soomter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventDetailData {

    @SerializedName("Status")
    @Expose
    public Integer status;
    @SerializedName("Error")
    @Expose
    public Integer error;
    @SerializedName("Result")
    @Expose
    public Datum result = null;

    public static class Datum {
        @SerializedName("Id")
        @Expose
        public int Id;
        @SerializedName("HigriToDate")
        @Expose
        public String HigriToDate;
        @SerializedName("HigriFromDate")
        @Expose
        public String HigriFromDate;
        @SerializedName("timeFrom")
        @Expose
        public String timeFrom;
        @SerializedName("timeTo")
        @Expose
        public String timeTo;
        @SerializedName("DateFrom")
        @Expose
        public String DateFrom;
        @SerializedName("DateTo")
        @Expose
        public String DateTo;
        @SerializedName("Time")
        @Expose
        public String Time;
        @SerializedName("Location")
        @Expose
        public String Location;
        @SerializedName("BusinessCategory")
        @Expose
        public String BusinessCategory;
        @SerializedName("Status")
        @Expose
        public int Status;
        @SerializedName("Mobile")
        @Expose
        public String Mobile;
        @SerializedName("Telephone")
        @Expose
        public String Telephone;
        @SerializedName("Email")
        @Expose
        public String Email;
        @SerializedName("EventUrl")
        @Expose
        public String EventUrl;
        @SerializedName("Long")
        @Expose
        public String Longitude;
        @SerializedName("Lat")
        @Expose
        public String Latitude;
        @SerializedName("EventStatus")
        @Expose
        public int EventStatus;
        @SerializedName("Organizers")
        @Expose
        public List<Organizers> organizers;
        @SerializedName("Image")
        @Expose
        public String Image;
        @SerializedName("Title")
        @Expose
        public String Title;
        @SerializedName("Category")
        @Expose
        public String Category;
        @SerializedName("Details")
        @Expose
        public String Details;
        @SerializedName("Link")
        @Expose
        public String Link;
    }

    public static class Organizers {
        @SerializedName("Id")
        @Expose
        public int Id;
        @SerializedName("CompanyName")
        @Expose
        public String CompanyName;
        @SerializedName("TitleKey")
        @Expose
        public String TitleKey;
        @SerializedName("Summary")
        @Expose
        public String Summary;
        @SerializedName("ImageId")
        @Expose
        public int ImageId;
        @SerializedName("Image")
        @Expose
        public String Image;
    }
}
