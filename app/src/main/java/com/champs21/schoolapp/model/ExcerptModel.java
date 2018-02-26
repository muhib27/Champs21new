package com.champs21.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RR on 26-Feb-18.
 */

public class ExcerptModel {
    @SerializedName("rendered")
    @Expose
    private String rendered;

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }
}
