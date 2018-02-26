package com.example.sloff.tokentestempty;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Sloff on 2/25/2018.
 */

public interface UserClient {

    @POST("/api/login")
    Call<User> login(@Body Login login);

    @GET("/api/dashboard")
    Call<ResponseBody> getJSON(@Header("Authorization") String authToken);
}
