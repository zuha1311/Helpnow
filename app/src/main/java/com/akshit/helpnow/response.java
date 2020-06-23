package com.akshit.helpnow;

import com.google.gson.annotations.SerializedName;

public class response {
    @SerializedName("success")
    private boolean success;

    @SerializedName("name")
    private String name;

    @SerializedName("mobileNo")
    private String mobileNo;

    public boolean isSuccess() {
        return success;
    }

    public String getName() {
        return name;
    }

    public String getMobileNo() {
        return mobileNo;
    }
}
