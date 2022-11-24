package com.rustybike.response;


import com.rustybike.models.Restaurants;
import com.rustybike.models.Result;

public class CreateRestaurantRes {
    private Restaurants data;
    private Result result;

    public Restaurants getData() {
        return data;
    }

    public void setData(Restaurants data) {
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
