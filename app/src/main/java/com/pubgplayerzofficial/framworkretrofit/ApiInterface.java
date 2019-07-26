package com.pubgplayerzofficial.framworkretrofit;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("register.php")
    Call<String> getRegister(@Query("full_name") String full_name, @Query("deviceId") String deviceId, @Query("user_name") String user_name, @Query("email") String email
            , @Query("mobile_no") String mobile_no, @Query("password") String password, @Query("sponsor_code") String sponsor_code);


//    @POST("GetAlbumImages.php")
//    Call<GalleryImageResponseParser> getGalleryPagenationData(@Field("pn") int pageIndex);
}

