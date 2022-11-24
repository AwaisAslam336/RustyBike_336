package com.rustybike.response;

import com.rustybike.models.ApiToken;
import com.rustybike.models.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private ApiToken data;
    private Result result;

    public ApiToken getData() {
        return data;
    }

    public void setData(ApiToken data) {
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}