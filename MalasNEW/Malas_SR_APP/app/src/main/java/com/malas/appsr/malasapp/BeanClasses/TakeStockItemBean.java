package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

public class TakeStockItemBean implements Serializable {
    String product_id;
    String product_mrp;
    String sku_code;
    String product_name;
    String quantity = "0";
    boolean itemChanges = false;
    int inMiliLitreOrMilligram;
String Case_Size;
    public TakeStockItemBean(String product_id, String product_mrp, String sku_code, String product_name, String quantity, boolean itemChanges,String Case_Size) {
        this.product_id = product_id;
        this.product_mrp = product_mrp;
        this.sku_code = sku_code;
        this.product_name = product_name;
        this.quantity = quantity;
        this.itemChanges = itemChanges;
        this.Case_Size=Case_Size;
    }
    /*public TakeStockItemBean(String product_id, String product_mrp, String sku_code, String product_name, String quantity, boolean itemChanges) {
        this.product_id = product_id;
        this.product_mrp = product_mrp;
        this.sku_code = sku_code;
        this.product_name = product_name;
        this.quantity = quantity;
        this.itemChanges = itemChanges;

    }*/

    public String getCase_Size() {
        return Case_Size;
    }

    public void setCase_Size(String case_Size) {
        Case_Size = case_Size;
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

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isItemChanges() {
        return itemChanges;
    }

    public void setItemChanges(boolean itemChanges) {
        this.itemChanges = itemChanges;
    }


}