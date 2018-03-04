package com.champs21.schoolapp.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RR on 28-Feb-18.
 */

public class Embedded {
    @SerializedName("wp:featuredmedia")
    @Expose
//    private MediaDetails mediaDetails;
//
//    public MediaDetails getMediaDetails() {
//        return mediaDetails;
//    }
//
//    public void setMediaDetails(MediaDetails mediaDetails) {
//        this.mediaDetails = mediaDetails;
//    }
      private ArrayList<JsonObject> featureMedia;

    public ArrayList<JsonObject> getFeatureMedia() {
        return featureMedia;
    }

    public void setFeatureMedia(ArrayList<JsonObject> featureMedia) {
        this.featureMedia = featureMedia;
    }
    //    private List<MediaDetails> mediaDetails;
//
//    public List<MediaDetails> getMediaDetails() {
//        return mediaDetails;
//    }
//
//    public void setMediaDetails(List<MediaDetails> mediaDetails) {
//        this.mediaDetails = mediaDetails;
//    }
}
