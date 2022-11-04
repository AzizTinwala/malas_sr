package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Krishna on 11/20/2016.
 */

public class TakeOrderEditedItemProductList {
    String outletId;
    String item_id;
    String item_qty;
    String product_name;
    String catId;
    String catName;
    String order_uni_id;
    public TakeOrderEditedItemProductList(String item_id, String item_qty,String product_name,String catId,String catName,String outletId) {
        this.item_id = item_id;
        this.item_qty = item_qty;
        this.product_name=product_name;
        this.catId=catId;
        this.catName=catName;
        this.outletId=outletId;
    }

    public TakeOrderEditedItemProductList(String item_id, String item_qty) {
        this.item_id = item_id;
        this.item_qty = item_qty;
    }

    public String getOrder_uni_id() {
        return order_uni_id;
    }

    public void setOrder_uni_id(String order_uni_id) {
        this.order_uni_id = order_uni_id;
    }

    public TakeOrderEditedItemProductList(String product_id, String item_qty, String product_name, String catId, String catName, String outletId, String order_uni_id) {
        this.item_id = product_id;
        this.item_qty = item_qty;
        this.product_name=product_name;
        this.catId=catId;
        this.catName=catName;
        this.outletId=outletId;
        this.order_uni_id=order_uni_id;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }
}
