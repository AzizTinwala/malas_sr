package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 02-Oct-18.
 */

public class OutletDetailsInActivity {
    String actId;
    String outletId;
    String outletName;
    String asmId;
    String userNameOutlet;
    String passwordOutlet;
    String avg_qty="";
    String avg_taste="";
    String avg_packaging="";

    public OutletDetailsInActivity() {


    }

    public OutletDetailsInActivity(String actId, String asmId, String outletId, String outletName, String userNameOutlet, String passwordOutlet, String avg_qty, String avg_taste, String avg_packaging) {
        this.actId = actId;
        this.outletId = outletId;
        this.outletName = outletName;
        this.asmId = asmId;
        this.userNameOutlet = userNameOutlet;
        this.passwordOutlet = passwordOutlet;
        this.avg_qty=avg_qty;
        this.avg_taste=avg_taste;
        this.avg_packaging=avg_packaging;
    }

    public String getAvg_qty() {
        return avg_qty;
    }

    public void setAvg_qty(String avg_qty) {
        this.avg_qty = avg_qty;
    }

    public String getAvg_taste() {
        return avg_taste;
    }

    public void setAvg_taste(String avg_taste) {
        this.avg_taste = avg_taste;
    }

    public String getAvg_packaging() {
        return avg_packaging;
    }

    public void setAvg_packaging(String avg_packaging) {
        this.avg_packaging = avg_packaging;
    }

    public String getOutletName() {
        return outletName;
    }

    public OutletDetailsInActivity(String actId, String asmId, String outletId, String outletName, String userNameOutlet, String passwordOutlet) {
        this.actId = actId;
        this.outletId = outletId;
        this.outletName = outletName;
        this.asmId = asmId;
        this.userNameOutlet = userNameOutlet;
        this.passwordOutlet = passwordOutlet;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getAsmId() {
        return asmId;
    }

    public void setAsmId(String asmId) {
        this.asmId = asmId;
    }

    public String getUserNameOutlet() {
        return userNameOutlet;
    }

    public void setUserNameOutlet(String userNameOutlet) {
        this.userNameOutlet = userNameOutlet;
    }

    public String getPasswordOutlet() {
        return passwordOutlet;
    }

    public void setPasswordOutlet(String passwordOutlet) {
        this.passwordOutlet = passwordOutlet;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }


}
