package com.amenah.tareq.project1.RetrofitPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServece {

    @POST("/index/signup")
    Call<StanderResponse> signup(@Body SignUpUserModel user);


    @POST("/index/login")
    Call<StanderResponse> login(@Body LoginUserModel user);


//    @POST("/logout")
//    Call<LogoutResponse> logout(@Body LogoutUserModel user);

}
