package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 20-Apr-18.
 */

public class SaveData {
    String order_unique_Id;
    String distributorId;
    String userId;
    String outletId;
    String latitude;
    String longitude;
    String address;
    String withSSo;
    String withAsm;
    String timeTakenOrderOffline;

    public SaveData(String distributorId, String userId, String outletId, String latitude, String longitude, String address, String withSSo, String withAsm,String timeTakenOrderOffline) {
        this.distributorId = distributorId;
        this.userId = userId;
        this.outletId = outletId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.withSSo = withSSo;
        this.withAsm = withAsm;
        this.timeTakenOrderOffline=timeTakenOrderOffline;
    }

    public String getOrder_unique_Id() {
        return order_unique_Id;
    }

    public void setOrder_unique_Id(String order_unique_Id) {
        this.order_unique_Id = order_unique_Id;
    }

    public SaveData() {

    }

    public SaveData(String distributerId, String mUserid, String outletId, String latitude, String longitude, String addressStr, String withASM, String withSSO, String order_unique_Id, String dateTimeInMillisecond) {

        this.distributorId = distributerId;
        this.userId = mUserid;
        this.outletId = outletId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = addressStr;
        this.withSSo = withSSO;
        this.withAsm = withASM;
        this.order_unique_Id = order_unique_Id;
        this.timeTakenOrderOffline=dateTimeInMillisecond; }

    public String getTimeTakenOrderOffline() {
        return timeTakenOrderOffline;
    }

    public void setTimeTakenOrderOffline(String timeTakenOrderOffline) {
        this.timeTakenOrderOffline = timeTakenOrderOffline;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWithSSo() {
        return withSSo;
    }

    public void setWithSSo(String withSSo) {
        this.withSSo = withSSo;
    }

    public String getWithAsm() {
        return withAsm;
    }

    public void setWithAsm(String withAsm) {
        this.withAsm = withAsm;
    }
}
