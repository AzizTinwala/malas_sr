package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 12/3/2017.
 */

public class ProductivityBean {
    private String Date;
    private int outletCount;
    private String outletName;

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public ProductivityBean(String date, String outletName) {
        Date = date;
        this.outletName = outletName;
    }

    public ProductivityBean(String date, int outletCount) {
        Date = date;
        this.outletCount = outletCount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getOutletCount() {
        return outletCount;
    }

    public void setOutletCount(int outletCount) {
        this.outletCount = outletCount;
    }
}
