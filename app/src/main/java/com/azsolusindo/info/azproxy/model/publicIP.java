package com.azsolusindo.info.azproxy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class publicIP {
    @SerializedName("query")
    @Expose
    private String query;

    public String getQuery(){
        return query;
    }

    public String toString(){
        return "IP{"+"query='"+query+'}';
    }
}
