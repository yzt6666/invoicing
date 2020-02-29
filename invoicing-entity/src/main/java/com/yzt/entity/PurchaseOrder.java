package com.yzt.entity;

public class PurchaseOrder {
    private String orderID;
    private Integer supplierID;
    private Integer employeeID;
    private String orderDate;
    private String flag;
    private String remark;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "orderID='" + orderID + '\'' +
                ", supplierID=" + supplierID +
                ", employeeID=" + employeeID +
                ", orderDate='" + orderDate + '\'' +
                ", flag='" + flag + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
