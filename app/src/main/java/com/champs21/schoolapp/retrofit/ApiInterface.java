package com.champs21.schoolapp.retrofit;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by RR on 27-Dec-17.
 */

public interface ApiInterface {

    //@Headers({"clientAgent : ANDROID", "version : 1"})
    //@POST("api/user/register")
    //Call<ServerResponse> getUserValidity(@Body MyUser userLoginCredential);

    //@FormUrlEncoded
//    @POST("getquestion")
//    //Call<JsonElement> fees(@FieldMap HashMap<String, String> params);
//    Call<ServerResponse> getQusestion(@Body CallQuestion callQuestion);
   // Call<JSONObject> question(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/sciencerocks/getquestion")
    Call<JsonElement> getQusestion(@FieldMap Map<String, String> params);
    @POST("api/sciencerocks")
    Call<JsonElement> getTopics(@Body HashMap<String, String> params);
    @FormUrlEncoded
    @POST("api/sciencerocks/getscoreboard")
    Call<JsonElement> getLeaderBoard(@FieldMap Map<String, String> params);
}


