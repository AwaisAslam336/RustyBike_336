package com.rustybike.models;



public class Create {
    private int OrderId;

    private int RestaurantId;

    private String OrderDate;

    private int OrderCompanyId;

    private String OrderRefNumber;

    private String OrderStatus;

    private String AddedBy;

    private String AddedDate;

    public int getOrderId() {
        return OrderId;
    }

    public int getRestaurantId() {
        return RestaurantId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public int getOrderCompanyId() {
        return OrderCompanyId;
    }

    public String getOrderRefNumber() {
        return OrderRefNumber;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public String getAddedDate() {
        return AddedDate;
    }
}
