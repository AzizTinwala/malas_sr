package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by xyz on 11/20/2016.
 */

public class TakeStockUserSaved implements Serializable {
    String stock_uni_id;
    String stock_time;
    String is_stock_placed;
    ArrayList<TakeStoclListBean> stockList;

    public String getIs_stock_placed() {
        return is_stock_placed;
    }

    public void setIs_stock_placed(String is_stock_placed) {
        this.is_stock_placed = is_stock_placed;
    }

    public TakeStockUserSaved(String stock_uni_id, String stock_time, String is_stock_placed, ArrayList<TakeStoclListBean> stockList) {
        this.stock_uni_id = stock_uni_id;
        this.stock_time = stock_time;
        this.is_stock_placed = is_stock_placed;

        this.stockList = stockList;
    }

    public String getStock_uni_id() {
        return stock_uni_id;
    }

    public void setStock_uni_id(String stock_uni_id) {
        this.stock_uni_id = stock_uni_id;
    }

    public String getStock_time() {
        return stock_time;
    }

    public void setStock_time(String stock_time) {
        this.stock_time = stock_time;
    }

    public ArrayList<TakeStoclListBean> getStockList() {
        return stockList;
    }

    public void setStockList(ArrayList<TakeStoclListBean> stockList) {
        this.stockList = stockList;
    }

    public static class OrderByTimeStampComparator implements Comparator<TakeStockUserSaved> {
        @Override
        public int compare(TakeStockUserSaved o1, TakeStockUserSaved o2) {
            if (Long.parseLong(o1.getStock_time()) > Long.parseLong(o2.getStock_time())) {
                return 1;
            } else if (Long.parseLong(o1.getStock_time()) < Long.parseLong(o2.getStock_time())) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
