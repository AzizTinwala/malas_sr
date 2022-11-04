package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 11-Apr-18.
 */

public class OutletOrderDateBean {
    String distributorId;
    String routeID;
    String outletId;
    String orderDate;
    String highlightOrNot;

    public OutletOrderDateBean(String distributorId, String routeID, String outletId, String orderDate,String highlightOrNot) {
        this.distributorId = distributorId;
        this.routeID = routeID;
        this.outletId = outletId;
        this.orderDate = orderDate;
        this.highlightOrNot=highlightOrNot;
    }

    public String getHighlightOrNot() {
        return highlightOrNot;
    }

    public void setHighlightOrNot(String highlightOrNot) {
        this.highlightOrNot = highlightOrNot;
    }

    public OutletOrderDateBean() {

    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
