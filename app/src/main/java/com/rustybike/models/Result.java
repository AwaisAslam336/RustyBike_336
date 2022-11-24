package com.rustybike.models;

public class Result {

    private int Rflag;
    private int Code;
    private String Message;
    private String Description;

    public int getRflag() {
        return Rflag;
    }

    public void setRflag(int rflag) {
        Rflag = rflag;
    }

    public  int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
