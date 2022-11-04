package com.malas.appsr.malasapp.BeanClasses;

import java.util.ArrayList;

/**
 * Created by Admin on 04-Feb-18.
 */

public class PreviousPlaceorderArraylistBean {
    String id,cat_name;
    ArrayList<TakePreviousPlaceOrderItemList> arryItemList;
    public PreviousPlaceorderArraylistBean(String id, String cat_name, ArrayList<TakePreviousPlaceOrderItemList> arryItemList) {
        this.id = id;
        this.cat_name = cat_name;
        this.arryItemList = arryItemList;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getItem() {
        return cat_name;
    }
    public void setItem(String item) {
        this.cat_name = item;
    }
    public ArrayList<TakePreviousPlaceOrderItemList> getArryItemList() {
        return arryItemList;
    }
    public void setArryItemList(ArrayList<TakePreviousPlaceOrderItemList> arryItemList) {
        this.arryItemList = arryItemList;
    }
}
