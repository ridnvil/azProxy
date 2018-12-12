package com.azsolusindo.info.azproxy.remote;

import com.azsolusindo.info.azproxy.model.publicIP;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IPService {
    @POST("/json")
    @FormUrlEncoded
    Call<publicIP> getPublicIP(@Field("query") String query);
}
