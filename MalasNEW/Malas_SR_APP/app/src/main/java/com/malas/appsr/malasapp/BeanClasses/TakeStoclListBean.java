package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class TakeStoclListBean implements Serializable{
    String id,cat_name;
    ArrayList<TakeStockItemBean> arryItemList;
    public TakeStoclListBean(String id, String cat_name, ArrayList<TakeStockItemBean> arryItemList) {
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
    public ArrayList<TakeStockItemBean> getArryItemList() {
        return arryItemList;
    }
    public void setArryItemList(ArrayList<TakeStockItemBean> arryItemList) {
        this.arryItemList = arryItemList;
    }
}