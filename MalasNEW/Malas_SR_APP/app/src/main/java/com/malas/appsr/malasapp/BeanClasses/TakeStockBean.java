package com.malas.appsr.malasapp.BeanClasses;



public class TakeStockBean {

    String stock,date;

    public TakeStockBean(String stock, String date) {
        this.stock = stock;
        this.date = date;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
