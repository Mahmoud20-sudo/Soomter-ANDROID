package com.soomter;

import com.google.gson.annotations.SerializedName;

public class NotificationsRequest {
    @SerializedName("UserId")
    public String UserId;
    @SerializedName("PageSize")
    public int PageSize;
    @SerializedName("PageNumber")
    public int PageNumber;

    public NotificationsRequest(String UserId,int PageSize, int PageNumber) {
        this.UserId = UserId;
        this.PageNumber = PageNumber;
        this.PageSize = PageSize;
    }
}
