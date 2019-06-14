package com.amenah.tareq.project1.RetrofitPackage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {


    public static Retrofit retrofit;
    public static ApiServece retrofitManager;
    private static String BASE_URL;

    public static void set(String url) {
        BASE_URL = url;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitManager = retrofit.create(ApiServece.class);

    }



}
