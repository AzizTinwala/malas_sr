package com.malas.appsr.malasapp.BeanClasses;

public class JointWorkBean {
    Integer userID, desig;
    String userName;
    public JointWorkBean(){}

    public JointWorkBean(Integer userID, String userName, Integer desig) {
        this.userID = userID;
        this.userName = userName;
        this.desig = desig;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getDesig() {
        return desig;
    }

    public void setDesig(Integer desig) {
        this.desig = desig;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
