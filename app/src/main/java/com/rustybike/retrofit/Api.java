package com.rustybike.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {
    private static Retrofit retrofit = null;
    public static ApiInterface getClient() {
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://emoney.kapookeu.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }

}
