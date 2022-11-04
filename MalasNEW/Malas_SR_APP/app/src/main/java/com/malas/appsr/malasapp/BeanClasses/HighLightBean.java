package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 14-Mar-18.
 */

public class HighLightBean {
    String outletId;
    String outletDate;
    String highlightOrNot;

    public HighLightBean(String outletId, String outletDate) {
        this.outletId = outletId;
        this.outletDate = outletDate;
    }

    public HighLightBean(String outletId, String outletDate, String highlightOrNot) {
        this.outletId = outletId;
        this.outletDate = outletDate;
        this.highlightOrNot = highlightOrNot;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOutletDate() {
        return outletDate;
    }

    public void setOutletDate(String outletDate) {
        this.outletDate = outletDate;
    }

    public String getHighlightOrNot() {
        return highlightOrNot;
    }

    public void setHighlightOrNot(String highlightOrNot) {
        this.highlightOrNot = highlightOrNot;
    }
}
