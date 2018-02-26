package com.champs21.schoolapp.utils;

import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.Wrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


/**
 * Created by RR on 31-Dec-17.
 */

public class GsonParser {
    // Singleton Stuffs
    private static GsonParser instance = null;
    private Gson gson;

    private GsonParser() {
        // Exists only to defeat instantiation.
        gson = new Gson();
    }

    public static synchronized GsonParser getInstance() {
        if (instance == null) {
            instance = new GsonParser();
        }
        return instance;
    }

    public Wrapper parseServerResponse2(JsonElement object) {
        Wrapper wrapper = gson.fromJson(object, Wrapper.class);
        return wrapper;
    }
    public CategoryModel parseServerResponseP(JsonElement object) {
        CategoryModel wrapper = gson.fromJson(object.toString(), CategoryModel.class);
        return wrapper;
    }
}
