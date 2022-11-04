package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 03-May-18.
 */

public class ReasonSubmitBean {
    String userId;
    String outletId;
    String reasonId;
    String distributorId;
    String latitude;
    String longitude;
    String address;
    String outletName;
    String reason;
    String routeId;

    public ReasonSubmitBean(String userId, String outletId, String reasonId, String distributorId, String latitude, String longitude, String address,String outletName,String reason,String routeId) {
        this.userId = userId;
        this.outletId = outletId;
        this.reasonId = reasonId;
        this.distributorId = distributorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.outletName=outletName;
        this.reason=reason;
        this.routeId=routeId;

    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public ReasonSubmitBean() {


    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
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

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
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
}
