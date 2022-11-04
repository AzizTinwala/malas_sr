package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Krishna on 11/20/2016.
 */

public class TakeStockEditedItemProductList {
    String item_id;
    String item_name;
    String item_qty;
    String item_case_size;

    public TakeStockEditedItemProductList(){}
    public TakeStockEditedItemProductList(String item_id, String item_qty, String item_case_size) {
        this.item_id = item_id;
        this.item_qty = item_qty;
        this.item_case_size = item_case_size;
    }

    public TakeStockEditedItemProductList(String item_id, String item_qty, String item_case_size,String item_name) {
        this.item_id = item_id;
        this.item_qty = item_qty;
        this.item_case_size = item_case_size;
        this.item_name=item_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_case_size() {
        return item_case_size;
    }

    public void setItem_case_size(String item_case_size) {
        this.item_case_size = item_case_size;
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
