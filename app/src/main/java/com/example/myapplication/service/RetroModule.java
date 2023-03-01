package com.example.myapplication.service;

import static com.example.myapplication.MyApplication.SERVER_KEY;

import com.example.myapplication.model.RequestNotification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetroModule {

    @Headers({"Content-Type:application/json", "Authorization:key="+SERVER_KEY})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RequestNotification requestNotification);

}
