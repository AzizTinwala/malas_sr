package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Krishna on 9/20/2016.
 */
public class RetailerBean {
    String retailerId;
    String retailerType;
    String activationStatus;

    public RetailerBean(String retailerId, String retailerType, String activationStatus) {
        this.retailerId = retailerId;
        this.retailerType = retailerType;
        this.activationStatus = activationStatus;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerType() {
        return retailerType;
    }

    public void setRetailerType(String retailerType) {
        this.retailerType = retailerType;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    @Override
    public String toString() {
        return "RetailerBean{" +
                "retailerId='" + retailerId + '\'' +
                ", retailerType='" + retailerType + '\'' +
                ", activationStatus='" + activationStatus + '\'' +
                '}';
    }
}
