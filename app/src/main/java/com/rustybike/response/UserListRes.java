package com.rustybike.response;

import com.rustybike.models.Result;
import com.rustybike.models.Users;

import java.util.List;

public class UserListRes {
    private Result result;
    private List<Users> data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Users> getData() {
        return data;
    }

    public void setData(List<Users> data) {
        this.data = data;
    }
}
