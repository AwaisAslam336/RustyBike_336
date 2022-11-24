package com.rustybike.response;

import com.rustybike.models.Result;
import com.rustybike.models.Users;

import java.util.List;

public class AddUserRes {
    private Result result;
    private Users data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Users getData() {
        return data;
    }

    public void setData(Users data) {
        this.data = data;
    }
}
