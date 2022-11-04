package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 04-Oct-18.
 */

public class CustomerData {
    String id;
    String cname;
    String cemail;
    String ccontact;
    String sold;
    String avg_qty="";
    String avg_taste="";
    String avg_packaging="";

    public CustomerData(String id, String cname, String cemail, String ccontact,String sold,String avg_qty,String avg_taste,String avg_packaging) {
        this.id = id;
        this.cname = cname;
        this.cemail = cemail;
        this.ccontact = ccontact;
        this.sold = sold;
        this.avg_qty=avg_qty;
        this.avg_taste=avg_taste;
        this.avg_packaging=avg_packaging;

    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public String getCcontact() {
        return ccontact;
    }

    public void setCcontact(String ccontact) {
        this.ccontact = ccontact;
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
}
