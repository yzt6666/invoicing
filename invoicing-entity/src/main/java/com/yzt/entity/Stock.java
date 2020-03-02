package com.yzt.entity;

public class Stock {
    private Integer stockID;
    private Integer productID;
    private Integer unitsInStock;
    private String stockArea;

    public Integer getStockID() {
        return stockID;
    }

    public void setStockID(Integer stockID) {
        this.stockID = stockID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(Integer unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public String getStockArea() {
        return stockArea;
    }

    public void setStockArea(String stockArea) {
        this.stockArea = stockArea;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockID=" + stockID +
                ", productID=" + productID +
                ", unitsInStock=" + unitsInStock +
                ", stockArea='" + stockArea + '\'' +
                '}';
    }
}
