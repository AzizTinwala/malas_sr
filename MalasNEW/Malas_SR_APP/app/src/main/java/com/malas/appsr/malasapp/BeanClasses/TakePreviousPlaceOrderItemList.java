package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 04-Feb-18.
 */

public  class TakePreviousPlaceOrderItemList {
    String product_id;
    String product_mrp;
    String sku_code;
    String product_name;
    String oredreQuantity;
    String stockQuantity;
    String difference;
    String packetSize;




    public TakePreviousPlaceOrderItemList(String product_id, String product_mrp, String sku_code, String product_name, String oredreQuantity, String stockQuantity, String difference, String packetSize) {
        this.product_id = product_id;
        this.product_mrp = product_mrp;
        this.sku_code = sku_code;
        this.product_name = product_name;
        this.oredreQuantity = oredreQuantity;
        this.stockQuantity = stockQuantity;
        this.difference = difference;
        this.packetSize = packetSize;

    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public void setProduct_mrp(String product_mrp) {
        this.product_mrp = product_mrp;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOredreQuantity() {
        return oredreQuantity;
    }

    public void setOredreQuantity(String oredreQuantity) {
        this.oredreQuantity = oredreQuantity;
    }

    public String getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(String packetSize) {
        this.packetSize = packetSize;
    }
}
