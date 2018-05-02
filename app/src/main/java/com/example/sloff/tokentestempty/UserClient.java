package com.example.sloff.tokentestempty;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Sloff on 2/25/2018.
 */

public interface UserClient {

    @POST("/api/login")
    Call<User> login(@Body Login login);

    @GET("/api/dashboard")
    Call<ResponseBody> getJSON(@Header("Authorization") String authToken);

    @GET//("/api/{docID}")
    Call<ResponseBody> getDocBody(@Url String url);

    @Multipart
    @POST("/api/upload")
    Call<ResponseBody>uploadPhoto(@Part MultipartBody.Part photo);
}
