package com.yzt.entity;

public class SaleOrderDetail {
    private String orderID;
    private Integer productID;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "SaleOrderDetail{" +
                "orderID='" + orderID + '\'' +
                ", productID=" + productID +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
