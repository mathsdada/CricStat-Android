package com.mission.cricstat.Rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Rest {
    private static <T> T builder(Class<T> endpoint) {
        return new Retrofit.Builder()
                .baseUrl("http://cricstat-rest-api.ap-south-1.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(endpoint);
    }

    public static API api() {
        return builder(API.class);
    }
}