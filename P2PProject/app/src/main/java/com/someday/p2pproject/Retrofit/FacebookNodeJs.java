package com.someday.p2pproject.Retrofit;

import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FacebookNodeJs {

    @POST("/auth/android/facebook")
    Observable<String> registerfbUser(@Body JsonObject gsonObject);

}
