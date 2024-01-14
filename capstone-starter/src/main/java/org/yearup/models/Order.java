package org.yearup.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private Date date = new Date(System.currentTimeMillis());

    private Profile profile;
    private BigDecimal shippingAmount;
    public List<OrderLineItem> orderLineItems;


    public Order() {
    }

    public Order(int orderId, int userId, Date date, Profile profile, BigDecimal shippingAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.profile = profile;
        this.shippingAmount = shippingAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }
}