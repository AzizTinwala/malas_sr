package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 12/7/2017.
 */

public class UniqueBillBean {
    String outletName;
    int outletCount;
    String unitime;
    String categoryId;
    String routeName;

    public UniqueBillBean(String outletName, int outletCount,String routeName) {
        this.outletName = outletName;
        this.outletCount = outletCount;
        this.routeName=routeName;

    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public int getOutletCount() {
        return outletCount;
    }

    public void setOutletCount(int outletCount) {
        this.outletCount = outletCount;
    }

    public String getUnitime() {
        return unitime;
    }

    public void setUnitime(String unitime) {
        this.unitime = unitime;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
