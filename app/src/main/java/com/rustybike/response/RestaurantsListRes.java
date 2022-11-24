package com.rustybike.response;

import com.rustybike.models.Restaurants;
import com.rustybike.models.Result;

import java.util.List;

public class RestaurantsListRes {
    private Result result;
    private List<Restaurants> data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Restaurants> getData() {
        return data;
    }

    public void setData(List<Restaurants> data) {
        this.data = data;
    }
}
