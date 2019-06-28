package com.amenah.tareq.project1.ConnectionManager.RetrofitPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServece {

    @POST("/index/signup")
    Call<StanderResponse> signup(@Body SignUpUserModel user);


    @POST("/index/login")
    Call<StanderResponse> login(@Body LoginUserModel user);


    @GET("/user/friends/{username}")
    Call<StanderResponse> getFriends(@Path("username") String username);


    @GET("/user/search/{query}")
    Call<StanderResponse> searchOnUsers(@Path("query") String query);

    @POST("/user/addFriend")
    Call<StanderResponse> addFriend(@Body AddFriendModel user);


//    @POST("/logout")
//    Call<LogoutResponse> logout(@Body LogoutUserModel user);

}
