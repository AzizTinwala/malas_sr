package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

public class TakeOutletOrderItemBean implements Serializable {
    String distributorId;
    private  String cat_id;
    String  catName;
    private String outlet_id;
    String product_id;
    String orderUniqueID;
    String product_qty;
    String product_name;
    String product_mrp;
    String sku_code;
    boolean itemChanges = false;

    public TakeOutletOrderItemBean(String product_id, String product_qty, String product_name, String product_mrp, String sku_code, boolean itemChanges) {
        this.product_id = product_id;
        this.product_qty = product_qty;
        this.product_name = product_name;
        this.product_mrp = product_mrp;
        this.sku_code = sku_code;
        this.itemChanges = itemChanges;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public TakeOutletOrderItemBean(String product_id, String product_qty, String product_name, String product_mrp, String sku_code, String outlet_id, String cat_id, String orderUniqueID, String catName) {
        this.product_id = product_id;
        this.product_qty = product_qty;
        this.product_name = product_name;
        this.product_mrp = product_mrp;
        this.sku_code = sku_code;
        this.cat_id = cat_id;
        this.catName=catName;
        this.outlet_id = outlet_id;
        this.orderUniqueID = orderUniqueID;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public TakeOutletOrderItemBean( String cat_id, String catName, String product_id,  String product_name,String product_qty,String outlet_id,String orderUniqueID) {
        this.cat_id = cat_id;
        this.catName = catName;
        this.product_id = product_id;
        this.product_qty = product_qty;
        this.product_name = product_name;
        this.outlet_id=outlet_id;
        this.orderUniqueID = orderUniqueID;
    }

    public TakeOutletOrderItemBean() {


    }

    public String getOrderUniqueID() {
        return orderUniqueID;
    }

    public void setOrderUniqueID(String orderUniqueID) {
        this.orderUniqueID = orderUniqueID;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public boolean isItemChanges() {
        return itemChanges;
    }

    public void setItemChanges(boolean itemChanges) {
        this.itemChanges = itemChanges;
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
}