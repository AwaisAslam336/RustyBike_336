package com.rustybike.models;

public class ApiToken {

    private int user_id;
    private String issue_on;
    private String expire_on;
    private String auth_token;
    private String auth_key;
    private int restaurant_id;
    private String login_type;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getIssue_on() {
        return issue_on;
    }

    public void setIssue_on(String issue_on) {
        this.issue_on = issue_on;
    }

    public String getExpire_on() {
        return expire_on;
    }

    public void setExpire_on(String expire_on) {
        this.expire_on = expire_on;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }
}
