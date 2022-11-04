package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/8/2016.
 */
public class CityListBean {

    String district_id,cid,cname;

    public CityListBean(String district_id, String cid, String cname) {
        this.district_id = district_id;
        this.cid = cid;
        this.cname = cname;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
