package com.champs21.schoolapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("title")
    @Expose
    private Title title;


    @SerializedName("excerpt")
    @Expose
    private ExcerptModel excerptModel;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public ExcerptModel getExcerptModel() {
        return excerptModel;
    }

    public void setExcerptModel(ExcerptModel excerptModel) {
        this.excerptModel = excerptModel;
    }
}