package com.azsolusindo.info.azproxy;

import com.azsolusindo.info.azproxy.remote.IPService;
import com.azsolusindo.info.azproxy.remote.RetrofitClient;

public class Common {
    private static final String BASE_URL = "http://ip-api.com/";

    public static IPService getIPService(){
        return RetrofitClient.getClient(BASE_URL).create(IPService.class);
    }
}
