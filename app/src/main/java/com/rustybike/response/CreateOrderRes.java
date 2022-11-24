package com.rustybike.response;

import com.rustybike.models.Create;
import com.rustybike.models.Result;

public class CreateOrderRes {
    private Create data;
    private Result result;

    public Create getData() {
        return data;
    }

    public void setData(Create data) {
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
