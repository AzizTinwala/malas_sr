package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/8/2016.
 */
public class CountryListBean {

    String countryId,countryName;

    public CountryListBean(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
