package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceOrderListBean implements Serializable {
    String id, cat_name;
    ArrayList<PlaceOrderItemBean> arryItemList;

    public PlaceOrderListBean(String id, String cat_name, ArrayList<PlaceOrderItemBean> arryItemList) {
        this.id = id;
        this.cat_name = cat_name;
        this.arryItemList = arryItemList;
    }

    public PlaceOrderListBean(PlaceOrderListBean p) {
        this.id = p.id;
        this.cat_name = p.cat_name;
        this.arryItemList =new ArrayList<>(p.arryItemList);
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

    public ArrayList<PlaceOrderItemBean> getArryItemList() {
        return arryItemList;
    }

    public void setArryItemList(ArrayList<PlaceOrderItemBean> arryItemList) {
        this.arryItemList = arryItemList;
    }
}