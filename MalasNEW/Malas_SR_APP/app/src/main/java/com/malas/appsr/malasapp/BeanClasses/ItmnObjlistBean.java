package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 11/20/2016.
 */

public class ItmnObjlistBean {

    String cat_id ;
    String sku_code;
    String product_id ;
    String product_mrp ;
    String cat_name ;
    String product_name ;

    public ItmnObjlistBean(String cat_id, String sku_code, String product_id, String product_mrp, String cat_name, String product_name) {
        this.cat_id = cat_id;
        this.sku_code = sku_code;
        this.product_id = product_id;
        this.product_mrp = product_mrp;
        this.cat_name = cat_name;
        this.product_name = product_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public void setProduct_mrp(String product_mrp) {
        this.product_mrp = product_mrp;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
