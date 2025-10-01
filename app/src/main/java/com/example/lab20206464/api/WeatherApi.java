package com.example.lab20206464.api;

import com.example.lab20206464.models.Location;
import com.example.lab20206464.models.SportsResponse;
import com.example.lab20206464.models.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    
    @GET("search.json")
    Call<List<Location>> searchLocations(
            @Query("key") String apiKey,
            @Query("q") String location
    );
    
    @GET("forecast.json")
    Call<WeatherResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int days
    );
    
    @GET("sports.json")
    Call<SportsResponse> getSports(
            @Query("key") String apiKey,
            @Query("q") String location
    );
}