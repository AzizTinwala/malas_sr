package com.malas.appsr.malasapp.BeanClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TargetBean implements Serializable {
    @SerializedName("cat_id")
    String cat_id;
    @SerializedName("cat_name")
    String cat_name;
    @SerializedName("cat_achived")
    String cat_achived;
    @SerializedName("cat_per")
    String cat_per;
    @SerializedName("target")
    String target;
    @SerializedName("prodcut_info")
    ArrayList<TargetProductBean> targetProductList;

    public TargetBean(String cat_id, String cat_name, String cat_achived, String cat_per, String target, ArrayList<TargetProductBean> targetProductList) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_achived = cat_achived;
        this.cat_per = cat_per;
        this.target = target;
        this.targetProductList = targetProductList;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_achived() {
        return cat_achived;
    }

    public void setCat_achived(String cat_achived) {
        this.cat_achived = cat_achived;
    }

    public String getCat_per() {
        return cat_per;
    }

    public void setCat_per(String cat_per) {
        this.cat_per = cat_per;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<TargetProductBean> getTargetProductList() {
        return targetProductList;
    }

    public void setTargetProductList(ArrayList<TargetProductBean> targetProductList) {
        this.targetProductList = targetProductList;
    }
}