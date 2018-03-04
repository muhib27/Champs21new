package com.champs21.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RR on 04-Mar-18.
 */

public class MediaDetails {
    @SerializedName("media_details")
    @Expose
    private Object mediaSizes;

    public Object getMediaSizes() {
        return mediaSizes;
    }

    public void setMediaSizes(Object mediaSizes) {
        this.mediaSizes = mediaSizes;
    }
}
