package com.azsolusindo.info.azproxy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class publicIP {
    @SerializedName("query")
    @Expose
    private String query;

    @SerializedName("country")
    @Expose
    private String country;

    public String getQuery(){
        return query;
    }

    public String getCountry(){ return country; }

    public String toString(){
        return "IP{"+"query='"+query+'}';
    }
}
