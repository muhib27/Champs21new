package com.champs21.schoolapp.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryModelTest {

    private String id;
//    private String title;
    private String link;
//    private String excerpt;
    public List<Title> titleListl = new ArrayList<>();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

//    public String getExcerpt() {
//        return excerpt;
//    }
//
//    public void setExcerpt(String excerpt) {
//        this.excerpt = excerpt;
//    }


    public List<Title> getTitleListl() {
        return titleListl;
    }

    public void setTitleListl(List<Title> titleListl) {
        this.titleListl = titleListl;
    }
}