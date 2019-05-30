package com.amenah.tareq.project1.RetrofitPackage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {


    //TODO: enter the url
    private static final String BASE_URL = "http://192.168.1.6:8080";


    public static Retrofit retrofit  = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static  ApiServece retrofitManager = retrofit.create(ApiServece.class);


}
