package com.malas.appsr.malasapp.BeanClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TargetProductBean implements Serializable {
    @SerializedName("productname")
    String productname;
    @SerializedName("productsku")
    String productsku;
    @SerializedName("producttarget")
    String producttarget;
    @SerializedName("productarchive")
    String productarchive;
    @SerializedName("productpercentage")
    String productpercentage;

    public TargetProductBean(String productname, String productsku, String producttarget, String productarchive, String productpercentage) {
        this.productname = productname;
        this.productsku = productsku;
        this.producttarget = producttarget;
        this.productarchive = productarchive;
        this.productpercentage = productpercentage;
    }

    public String getProductname() {

        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductsku() {
        return productsku;
    }

    public void setProductsku(String productsku) {
        this.productsku = productsku;
    }

    public String getProducttarget() {
        return producttarget;
    }

    public void setProducttarget(String producttarget) {
        this.producttarget = producttarget;
    }

    public String getProductarchive() {
        return productarchive;
    }

    public void setProductarchive(String productarchive) {
        this.productarchive = productarchive;
    }

    public String getProductpercentage() {
        return productpercentage;
    }

    public void setProductpercentage(String productpercentage) {
        this.productpercentage = productpercentage;
    }
}