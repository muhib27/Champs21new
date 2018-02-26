package com.champs21.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RR on 18-Feb-18.
 */

public class CommonApiResponse {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("code")
    @Expose
    public int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
