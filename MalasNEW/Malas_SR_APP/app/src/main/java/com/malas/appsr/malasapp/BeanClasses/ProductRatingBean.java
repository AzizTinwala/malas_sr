package com.malas.appsr.malasapp.BeanClasses;

import java.util.ArrayList;

/**
 * Created by Arwa on 26-Sep-18.
 */

public class ProductRatingBean {
   String productName;
   ArrayList<String> qty;
   ArrayList<String> taste;
   String sku;
   ArrayList<String> packaging;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductRatingBean(String productName) {
        this.productName = productName;
    }

    public ProductRatingBean() {


    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ArrayList<String> getQty() {
        return qty;
    }

    public void setQty(ArrayList<String> qty) {

       this.qty=qty;
    }

    public ArrayList<String> getTaste() {
        return taste;
    }

    public void setTaste(ArrayList<String> taste) {

        this.taste = taste;
    }

    public ArrayList<String> getPackaging() {
        return packaging;
    }

    public void setPackaging(ArrayList<String> packaging) {

        this.packaging = packaging;
    }
}
