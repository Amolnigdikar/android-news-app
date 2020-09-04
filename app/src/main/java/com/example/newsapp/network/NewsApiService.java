package com.example.newsapp.network;

import android.content.res.Resources;

import com.example.newsapp.R;
import com.example.newsapp.model.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApiService {

    String API_KEY="add_your_api_key_here";

    @GET("top-headlines?country=in&apiKey="+API_KEY)
    Call<NewsResponse> getAllNews();

    @GET("top-headlines")
    Call<NewsResponse> getCategoryNews(@Query("country") String country,
                                       @Query("category") String category,
                                       @Query("apiKey") String apiKey);
}
