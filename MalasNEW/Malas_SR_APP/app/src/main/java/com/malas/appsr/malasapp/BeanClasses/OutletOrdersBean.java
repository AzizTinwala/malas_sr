package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Krishna on 9/11/2016.
 */
public class OutletOrdersBean implements Serializable {
    String order_unique_id;
    String order_take_time;
    String order_time_in_long;
    String is_order_placed;
    String withSso;
    String withAsm;
    String outletId;
    String distributorId;
    ArrayList<TakeOutletOrderListBean> productList;

    public OutletOrdersBean(String order_unique_id, String order_take_time, String is_order_placed, String withSso, String withAsm, ArrayList<TakeOutletOrderListBean> productList, String order_time_in_long,String distributorId,String outletId) {
        this.order_unique_id = order_unique_id;
        this.order_take_time = order_take_time;
        this.is_order_placed = is_order_placed;
        this.productList = productList;
        this.withAsm = withAsm;
        this.withSso = withSso;
        this.order_time_in_long = order_time_in_long;
        this.distributorId=distributorId;
        this.outletId=outletId;
    }

    public OutletOrdersBean(String order_time) {
        this.order_take_time = order_time;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public OutletOrdersBean(String order_unique_id, String order_take_time, String is_order_placed, String withSso, String withAsm, String order_time_in_long, String outletId) {

        this.order_unique_id = order_unique_id;
        this.order_take_time = order_take_time;
        this.is_order_placed = is_order_placed;
        this.outletId = outletId;
        this.withAsm = withAsm;
        this.withSso = withSso;
        this.order_time_in_long = order_time_in_long;
    }

    public OutletOrdersBean() {

    }

    public String getOrder_time_in_long() {
        return order_time_in_long;
    }

    public void setOrder_time_in_long(String order_time_in_long) {
        this.order_time_in_long = order_time_in_long;
    }

    public String getOrder_unique_id() {
        return order_unique_id;
    }

    public void setOrder_unique_id(String order_unique_id) {
        this.order_unique_id = order_unique_id;
    }

    public String getOrder_take_time() {
        return order_take_time;
    }

    public void setOrder_take_time(String order_take_time) {
        this.order_take_time = order_take_time;
    }

    public ArrayList<TakeOutletOrderListBean> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<TakeOutletOrderListBean> productList) {
        this.productList = productList;
    }

    public String getIs_order_placed() {
        return is_order_placed;
    }

    public void setIs_order_placed(String is_order_placed) {
        this.is_order_placed = is_order_placed;
    }

    public String getWithSso() {
        return withSso;
    }

    public void setWithSso(String withSso) {
        this.withSso = withSso;
    }

    public String getWithAsm() {
        return withAsm;
    }

    public void setWithAsm(String withAsm) {
        this.withAsm = withAsm;
    }

    public static class OrderByTimeStampComparator implements Comparator<OutletOrdersBean> {
        @Override
        public int compare(OutletOrdersBean o1, OutletOrdersBean o2) {
            if (Long.parseLong(o1.getOrder_time_in_long()) > Long.parseLong(o2.getOrder_time_in_long())) {
                return 1;
            } else if (Long.parseLong(o1.getOrder_time_in_long()) < Long.parseLong(o2.getOrder_time_in_long())) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}