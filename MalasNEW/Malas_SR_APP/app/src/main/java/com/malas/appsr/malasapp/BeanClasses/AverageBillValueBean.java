package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 11/27/2017.
 */

public class AverageBillValueBean {
    String outletCount;
    int value;
    String orderDate;

    public AverageBillValueBean(String orderDate, int value,String outletCount,  String billValue) {
        this.outletCount = outletCount;
        this.value = value;
        this.orderDate = orderDate;
        this.billValue = billValue;
    }

    String billValue;

    public String getBillValue() {
        return billValue;
    }

    public void setBillValue(String billValue) {
        this.billValue = billValue;
    }

    public String getOutletCount() {
        return outletCount;
    }

    public void setOutletCount(String outletCount) {
        this.outletCount = outletCount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }


}
