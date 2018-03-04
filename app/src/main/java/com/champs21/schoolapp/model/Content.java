package com.champs21.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RR on 28-Feb-18.
 */

public class Content {
    @SerializedName("rendered")
    @Expose
    private String mainConten;

    public String getMainConten() {
        return mainConten;
    }

    public void setMainConten(String mainConten) {
        this.mainConten = mainConten;
    }
}
