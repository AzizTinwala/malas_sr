package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 11/24/2017.
 */

public class AverageLineCutBean  {

    String date;
    String distributorName;
    int countOutletVisited;
    int categoryCount;
    int skuCount;
    int linecut;

    public AverageLineCutBean(String date, String distributorName, int countOutletVisited, int categoryCount, int skuCount, int lineCut) {
        this.date = date;
        this.distributorName = distributorName;
        this.countOutletVisited = countOutletVisited;
        this.categoryCount = categoryCount;
        this.skuCount = skuCount;
        this.linecut=lineCut;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public int getCountOutletVisited() {
        return countOutletVisited;
    }

    public void setCountOutletVisited(int countOutletVisited) {
        this.countOutletVisited = countOutletVisited;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public int getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(int skuCount) {
        this.skuCount = skuCount;
    }

    public int getLinecut() {
        return linecut;
    }

    public void setLinecut(int linecut) {
        this.linecut = linecut;
    }
}
