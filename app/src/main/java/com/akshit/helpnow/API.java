package com.akshit.helpnow;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    String BASE_URL = "https://gethelpnow.herokuapp.com";

    @POST("/user/signUp")
    Call<response> getOtp(@Body User user);

    @POST("/user/verify")
    Call<response> verifyOtp(@Body Verify verify);

    @GET("user/resend")
    Call<Response> resendOtp(
            @Query("mobileNo") String mobileNo );
}
