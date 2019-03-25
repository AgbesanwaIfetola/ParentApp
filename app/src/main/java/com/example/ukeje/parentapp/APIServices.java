package com.example.ukeje.parentapp;


import retrofit2.Call;
import retrofit2.http.GET;

public interface APIServices {

    @GET("hackathon/alert?id=1")
    Call<GetData> getLocation();

}
