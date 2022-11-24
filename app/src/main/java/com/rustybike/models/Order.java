package com.rustybike.models;


public class Order {
    private  int OrderId;
    private  int RestaurantId;
    private  String OrderDate;
    private  int OrderCompanyId;
    private  String OrderRefNumber;
    private  String OrderStatus;
    private  String AddedBy;
    private  String AddedDate;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    public int getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(int RestaurantId) {
        this.RestaurantId = RestaurantId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public int getOrderCompanyId() {
        return OrderCompanyId;
    }

    public void setOrderCompanyId(int OrderCompanyId) {
        this.OrderCompanyId = OrderCompanyId;
    }

    public String getOrderRefNumber() {
        return OrderRefNumber;
    }

    public void setOrderRefNumber(String OrderRefNumber) {
        this.OrderRefNumber = OrderRefNumber;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String AddedBy) {
        this.AddedBy = AddedBy;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String AddedDate) {
        this.AddedDate = AddedDate;
    }
}
