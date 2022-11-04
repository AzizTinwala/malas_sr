package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Krishna on 11/20/2016.
 */

public class PlaceOrderEditedItemProductList {
    String item_id;
    String item_qty;

    public PlaceOrderEditedItemProductList(String item_id, String item_qty) {
        this.item_id = item_id;
        this.item_qty = item_qty;
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
