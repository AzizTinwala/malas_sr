package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 24-Mar-19.
 */

public class TotalOutletCountHome {
    String totalOutlet="";
    String billedOutlet="";
    String unbilledOutlet="";

    String totalDS="";
    String billedDS="";
    String unbilledDS="";

    public String getTotalDS() {
        return totalDS;
    }

    public void setTotalDS(String totalDS) {
        this.totalDS = totalDS;
    }

    public String getBilledDS() {
        return billedDS;
    }

    public void setBilledDS(String billedDS) {
        this.billedDS = billedDS;
    }

    public String getUnbilledDS() {
        return unbilledDS;
    }

    public void setUnbilledDS(String unbilledDS) {
        this.unbilledDS = unbilledDS;
    }

    public TotalOutletCountHome(String totalOutlet, String billedOutlet, String unbilledOutlet) {
        this.totalOutlet = totalOutlet;
        this.billedOutlet = billedOutlet;
        this.unbilledOutlet = unbilledOutlet;
    }

    public TotalOutletCountHome() {

    }

    public String getTotalOutlet() {
        return totalOutlet;
    }

    public void setTotalOutlet(String totalOutlet) {
        this.totalOutlet = totalOutlet;
    }

    public String getBilledOutlet() {
        return billedOutlet;
    }

    public void setBilledOutlet(String billedOutlet) {
        this.billedOutlet = billedOutlet;
    }

    public String getUnbilledOutlet() {
        return unbilledOutlet;
    }

    public void setUnbilledOutlet(String unbilledOutlet) {
        this.unbilledOutlet = unbilledOutlet;
    }
}

