package com.yzt.entity;

public class SaleOrder {
    private String orderID;
    private String customerID;
    private Integer employeeID;
    private String orderDate;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipProvince;
    private String shipPostalCode;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipProvince() {
        return shipProvince;
    }

    public void setShipProvince(String shipProvince) {
        this.shipProvince = shipProvince;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    @Override
    public String toString() {
        return "SaleOrder{" +
                "orderID='" + orderID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", employeeID=" + employeeID +
                ", orderDate='" + orderDate + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", shipCity='" + shipCity + '\'' +
                ", shipProvince='" + shipProvince + '\'' +
                ", shipPostalCode='" + shipPostalCode + '\'' +
                '}';
    }
}
