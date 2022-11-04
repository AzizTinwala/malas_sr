package com.malas.appsr.malasapp.BeanClasses;


/**
 * Created by Arwa on 26-Sep-18.
 */

public class ProductRatingArrayBean {
   int position;
   String qty="";
   String taste="";
   String packaging="";
   String name="";
   String sku="";


    public ProductRatingArrayBean() {


    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
}
