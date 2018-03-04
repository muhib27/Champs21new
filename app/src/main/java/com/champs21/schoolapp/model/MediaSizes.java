package com.champs21.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RR on 04-Mar-18.
 */

public class MediaSizes {
    @SerializedName("thumbnail")
    @Expose
    private MediaThumbnail mediaThumbnail;

    public MediaThumbnail getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(MediaThumbnail mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }
}
