package com.someday.p2pproject.Retrofit;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserNodeJs {
    @POST("/auth/register")
    @FormUrlEncoded
    Observable<String>  registerUser(@Field("email") String email,
                                     @Field("name") String name,
                                     @Field("password") String password);

    @POST("/auth/loginuser")
    @FormUrlEncoded
    Observable<String>  loginUser(@Field("email") String email,
                                     @Field("password") String password);
}
