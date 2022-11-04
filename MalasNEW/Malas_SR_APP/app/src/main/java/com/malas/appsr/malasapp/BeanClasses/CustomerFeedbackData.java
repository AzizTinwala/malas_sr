package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 04-Oct-18.
 */

public class CustomerFeedbackData {
    String cid;
    String taste;
    String qty;
    String packaging;
    String productName;

    public CustomerFeedbackData(String cid, String qty, String taste, String packaging,String productName) {
        this.cid = cid;
        this.taste = taste;
        this.qty = qty;
        this.packaging = packaging;
        this.productName = productName;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
}
