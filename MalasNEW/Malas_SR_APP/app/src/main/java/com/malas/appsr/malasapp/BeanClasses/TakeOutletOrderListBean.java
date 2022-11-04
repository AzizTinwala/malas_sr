package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class TakeOutletOrderListBean implements Serializable {
    private String order_uni_id;
    private String outlet_id;
    String id, cat_name;
    ArrayList<TakeOutletOrderItemBean> arryItemList;

    /*public TakeOutletOrderListBean(String id, String cat_name, ArrayList<TakeOutletOrderItemBean> arryItemList,String outlet_id) {
        this.id = id;
        this.cat_name = cat_name;
        this.arryItemList = arryItemList;
        this
    }*/

    public TakeOutletOrderListBean(String id, String cat_name, ArrayList<TakeOutletOrderItemBean> arryItemList, String outlet_id) {
        this.id = id;
        this.cat_name = cat_name;
        this.arryItemList = arryItemList;
        this.outlet_id = outlet_id;
    }


    public TakeOutletOrderListBean(String cat_id, String cat_name, String order_uni_id, String outlet_id) {
        this.id = cat_id;
        this.cat_name = cat_name;
        this.order_uni_id = order_uni_id;
        this.outlet_id = outlet_id;
    }

    public TakeOutletOrderListBean() {

    }

    public TakeOutletOrderListBean(TakeOutletOrderListBean selectedCategory) {
        this.id=selectedCategory.id;
        this.cat_name= selectedCategory.cat_name;
        this.order_uni_id=selectedCategory.order_uni_id;
        this.outlet_id=selectedCategory.outlet_id;
        this.arryItemList = new ArrayList<>(selectedCategory.arryItemList);
    }

    public String getOrder_uni_id() {
        return order_uni_id;
    }

    public void setOrder_uni_id(String order_uni_id) {
        this.order_uni_id = order_uni_id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
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

    public ArrayList<TakeOutletOrderItemBean> getArryItemList() {
        return arryItemList;
    }

    public void setArryItemList(ArrayList<TakeOutletOrderItemBean> arryItemList) {
        this.arryItemList = arryItemList;
    }
}