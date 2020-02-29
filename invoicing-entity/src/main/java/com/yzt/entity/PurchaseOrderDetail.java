package com.yzt.entity;

public class PurchaseOrderDetail {
    private String orderID;
    private Integer quantity;
    private Integer productID;
    private Double totalPrice;
    private Double purchasePrice;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetail{" +
                "orderID='" + orderID + '\'' +
                ", quantity=" + quantity +
                ", productID=" + productID +
                ", totalPrice=" + totalPrice +
                ", purchasePrice=" + purchasePrice +
                '}';
    }
}
