package com.example.newzandeducation.API;



import com.example.newzandeducation.models.newz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("top-headlines")
    Call<newz> getnewz (
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
    @GET("everything")
    Call<newz> getnewzSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey


    );
    }

