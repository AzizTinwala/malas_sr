package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 21-Mar-19.
 */

public class FocusedProductBean {
    String productName;
    String month;
    String year;

    public FocusedProductBean(String productName, String month, String year) {
        this.productName = productName;
        this.month = month;
        this.year = year;
    }

    public FocusedProductBean() {

    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public FocusedProductBean(String productName) {
        this.productName = productName;
    }
}
