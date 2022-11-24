package com.rustybike.response;

import com.rustybike.models.Order;
import com.rustybike.models.Result;

import java.util.List;

public class OrderListRes {

    private Result result;
    private List<com.rustybike.models.Order> data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<com.rustybike.models.Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
