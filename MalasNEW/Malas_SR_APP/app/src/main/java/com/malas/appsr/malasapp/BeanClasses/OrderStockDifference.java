package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

/**
 * Created by ADV on 21-Feb-17.
 */

public class OrderStockDifference implements Serializable {

    String id;
    String name;
    String orderQty;
    String stockQty;
    String difference;
    String packetSize;
    String inboxSize;
    String skuCode;
    String categoryName;


    public OrderStockDifference(String id, String name, String orderQty, String stockQty, String difference, String packetSize, String skuCode, String categoryName, String inboxSize) {
        this.id = id;
        this.name = name;
        this.orderQty = orderQty;
        this.stockQty = stockQty;
        this.difference = difference;
        this.packetSize = packetSize;
        this.skuCode = skuCode;
        this.categoryName = categoryName;
        this.inboxSize = inboxSize;
    }


    public String getInboxSize() {
        return inboxSize;
    }

    public void setInboxSize(String inboxSize) {
        this.inboxSize = inboxSize;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStockDifference)) return false;

        OrderStockDifference that = (OrderStockDifference) o;

        if (!getId().equals(that.getId())) return false;
        return getCategoryName().equals(that.getCategoryName());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCategoryName().hashCode();
        return result;
    }
}
