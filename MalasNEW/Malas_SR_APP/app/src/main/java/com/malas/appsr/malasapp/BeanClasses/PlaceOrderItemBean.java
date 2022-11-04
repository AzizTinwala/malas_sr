package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

public class PlaceOrderItemBean implements Serializable {
    String id;
    String name;
    String orderQty;
    String stockQty;
    String difference;
    String packetSize;
    String inboxSize;
    String skuCode;
    String categoryName;

    public PlaceOrderItemBean(String id, String name, String orderQty, String stockQty, String difference, String packetSize, String inboxSize, String skuCode, String categoryName) {
        this.id = id;
        this.name = name;
        this.orderQty = orderQty;
        this.stockQty = stockQty;
        this.difference = difference;
        this.packetSize = packetSize;
        this.inboxSize = inboxSize;
        this.skuCode = skuCode;
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
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

    public String getInboxSize() {
        return inboxSize;
    }

    public void setInboxSize(String inboxSize) {
        this.inboxSize = inboxSize;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}