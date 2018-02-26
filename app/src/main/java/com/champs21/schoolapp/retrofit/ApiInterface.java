package com.champs21.schoolapp.retrofit;

import android.view.SurfaceHolder;

import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.CategoryModelList;
import com.champs21.schoolapp.model.CategoryModelTest;
import com.champs21.schoolapp.model.CommonApiResponse;
import com.champs21.schoolapp.model.TopRatedMovies;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


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
//    @POST("api/sciencerocks")
//    Observable<Response<JsonElement>> getTopics(@Body HashMap<String, String> params);
    @FormUrlEncoded
    @POST("api/sciencerocks/getscoreboard")
    Call<JsonElement> getLeaderBoard(@FieldMap Map<String, String> params);
    @GET("movie/top_rated")
    Call<TopRatedMovies>getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int pageIndex);

//    @GET("movie/top_rated")
//    Observable<Response<TopRatedMovies> >getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int pageIndex);

    @GET("posts")
    Call<JsonElement> getTopics(@Query("categories") int post, @Query("per_page") int per_page);

    @GET("posts")
    Call<List<CategoryModel>> getTopicsList(@Query("categories") int post, @Query("per_page") int per_page);
}


