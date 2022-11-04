package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 11-Apr-18.
 */

public class StockPlaced {
    String distributorId;
    String userId;
    String stock_placed;
    String is_order_placed;
    String outletId;
    public StockPlaced() {

    }
    public StockPlaced(String distributorId, String userId, String stock_placed) {
        this.distributorId = distributorId;
        this.userId = userId;
        this.stock_placed = stock_placed;
    }



    public StockPlaced(String distributorId, String userId, String stock_placed, String is_order_placed) {
        this.distributorId = distributorId;
        this.userId = userId;
        this.stock_placed = stock_placed;
        this.is_order_placed = is_order_placed;
    }


    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getIs_order_placed() {
        return is_order_placed;
    }

    public void setIs_order_placed(String is_order_placed) {
        this.is_order_placed = is_order_placed;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStock_placed() {
        return stock_placed;
    }

    public void setStock_placed(String stock_placed) {
        this.stock_placed = stock_placed;
    }
}
