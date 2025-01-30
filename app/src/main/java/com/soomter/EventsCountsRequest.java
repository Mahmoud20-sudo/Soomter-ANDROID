package com.soomter;

import com.google.gson.annotations.SerializedName;

public class EventsCountsRequest {

    @SerializedName("Lang")
    public String 	Lang;
    @SerializedName("EventTo")
    public int 	EventTo;

    public EventsCountsRequest(Integer EventTo , String Lang) {
        this.EventTo = EventTo;
        this.Lang = Lang;
    }
}
