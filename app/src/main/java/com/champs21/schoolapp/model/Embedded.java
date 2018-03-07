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
    private ArrayList<JsonObject> featureMedia;

    @SerializedName("author")
    @Expose
    private ArrayList<JsonObject> author;

    public ArrayList<JsonObject> getFeatureMedia() {
        return featureMedia;
    }

    public void setFeatureMedia(ArrayList<JsonObject> featureMedia) {
        this.featureMedia = featureMedia;
    }

    public ArrayList<JsonObject> getAuthor() {
        return author;
    }

    public void setAuthor(ArrayList<JsonObject> author) {
        this.author = author;
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
