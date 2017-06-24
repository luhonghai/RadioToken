package com.halosolutions.library.data;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by luhonghai on 6/24/17.
 */

public interface RadioService {

    String BASE_URL = "https://radiko.jp/v2/api/";
    String RADIKO_APP = "aSmartPhone4";
    String RADIKO_APP_VERSION = "4.0.3";
    String RADIKO_USER = "test-stream";
    String RADIKO_DEVICE = "android";

    @POST("auth1_fms")
    @Headers({
            "pragma: no-cache",
            "X-Radiko-App: " + RADIKO_APP,
            "X-Radiko-App-Version: " + RADIKO_APP_VERSION,
            "X-Radiko-User: " + RADIKO_USER,
            "X-Radiko-Device: " + RADIKO_DEVICE
    })
    Call<String> auth1();

    @POST("auth2_fms")
    @Headers({
            "pragma: no-cache",
            "X-Radiko-App: " + RADIKO_APP,
            "X-Radiko-App-Version: " + RADIKO_APP_VERSION,
            "X-Radiko-User: " + RADIKO_USER,
            "X-Radiko-Device: " + RADIKO_DEVICE
    })
    Call<String> auth2(
            @Header("X-Radiko-Authtoken") String authToken,
            @Header("X-Radiko-Partialkey") String partialKey,
            @Header("X-Radiko-Location") String location);
}
