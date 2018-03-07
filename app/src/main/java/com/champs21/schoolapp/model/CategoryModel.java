package com.champs21.schoolapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryModel {
    //    @SerializedName("code")
//    @Expose
//    private String code;
//
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("date")
    @Expose
    private String newsDate;


    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("title")
    @Expose
    private Title title;

    @SerializedName("content")
    @Expose
    private Content content;


    @SerializedName("excerpt")
    @Expose
    private ExcerptModel excerptModel;

    public CategoryModel(String id, String link, Title title, Content content, ExcerptModel excerptModel) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.content = content;
        this.excerptModel = excerptModel;
    }

    public CategoryModel() {
    }

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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }
    //    public Object getEmbedded() {
//        return embedded;
//    }
//
//    public void setEmbedded(Object embedded) {
//        this.embedded = embedded;
//    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }
}