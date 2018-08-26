package com.example.farhan.popular_movies.SetUpRetrofit;

import com.example.farhan.popular_movies.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// I searched it from Retrofit docs
public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private Retrofit mRetrofit;
    private  TheMoviesServices theMoviesServices;

    private RetrofitClient(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.STATIC_MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        theMoviesServices = mRetrofit.create(TheMoviesServices.class);
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }

        return retrofitClient;
    }

    public TheMoviesServices getService() {
        return theMoviesServices;
    }

}
