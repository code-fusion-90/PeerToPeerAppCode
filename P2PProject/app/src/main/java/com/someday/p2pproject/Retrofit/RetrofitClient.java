package com.someday.p2pproject.Retrofit;

import android.bluetooth.le.ScanRecord;
import android.widget.Toast;

import com.someday.p2pproject.MainActivity;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance(){
            instance = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();


        return instance;

    }

    public static Retrofit getGsonInstance(){
        instance = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        return instance;

    }
}
