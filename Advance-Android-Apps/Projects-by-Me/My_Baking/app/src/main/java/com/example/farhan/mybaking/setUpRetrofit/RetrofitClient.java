package com.example.farhan.mybaking.setUpRetrofit;

import com.example.farhan.mybaking.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private  TheBakingServices theBakingServices;

    private RetrofitClient(){
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.STATIC_BAKING_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        theBakingServices = mRetrofit.create(TheBakingServices.class);
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }

        return retrofitClient;
    }

    public TheBakingServices getService() {
        return theBakingServices;
    }
}
