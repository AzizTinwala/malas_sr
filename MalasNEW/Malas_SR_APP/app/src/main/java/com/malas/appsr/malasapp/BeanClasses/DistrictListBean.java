package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/8/2016.
 */
public class DistrictListBean {

    String districtId,districtName,country_id;

    public DistrictListBean(String districtId, String districtName, String country_id) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.country_id = country_id;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
