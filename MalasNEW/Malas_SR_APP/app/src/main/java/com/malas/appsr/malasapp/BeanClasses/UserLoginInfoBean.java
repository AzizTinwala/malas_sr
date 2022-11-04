package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/10/2016.
 */
public class UserLoginInfoBean {

    String disName;
    String territoryName;
    String deviceType;
    String designationId;
    String asmId;
    String userAddress;
    String ssoId;
    String userDob;
    String userEmail;
    String userDistrictId;
    String countryName;
    String userName;
    String userTerritoryId;
    String userCountryId;
    String userActivationStatus;
    String userMobile;
    String userCityId;
    String userId;
    String cname;
    String asmName;
    String ssoName;
    String dateOfJoining;
    String salesType;
    String empCode;
    String weekOff;
    String userDepartment;
    String userDesignation;
    String userPinCode;
    String userCity;
    String userState;

    public UserLoginInfoBean(
            String disName, String territoryName, String deviceType, String designationId,
            String asmId, String userAddress, String ssoId, String userDob, String userEmail,
            String userDistrictId, String countryName, String userName, String userTerritoryId,
            String userCountryId, String userActivationStatus, String userMobile,
            String userCityId, String userId, String cname, String asmName, String ssoName,
            String dateOfJoining, String salesType, String empCode, String weekOff, String department,
            String designation, String pinCode, String hrCityName, String hrStateName
    ) {

        this.disName = disName;
        this.territoryName = territoryName;
        this.deviceType = deviceType;
        this.designationId = designationId;
        this.asmId = asmId;
        this.userAddress = userAddress;
        this.ssoId = ssoId;
        this.userDob = userDob;
        this.userEmail = userEmail;
        this.userDistrictId = userDistrictId;
        this.countryName = countryName;
        this.userName = userName;
        this.userTerritoryId = userTerritoryId;
        this.userCountryId = userCountryId;
        this.userActivationStatus = userActivationStatus;
        this.userMobile = userMobile;
        this.userCityId = userCityId;
        this.userId = userId;
        this.cname = cname;
        this.asmName = asmName;
        this.ssoName = ssoName;
        this.dateOfJoining = dateOfJoining;
        this.salesType = salesType;
        this.empCode = empCode;
        this.userDepartment = department;
        this.userDesignation = designation;
        this.userPinCode = pinCode;
        this.weekOff = weekOff;
        this.userCity = hrCityName;
        this.userState = hrStateName;

    }


    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public String getAsmName() {
        return asmName;
    }

    public String getSsoName() {
        return ssoName;
    }

    public String getDisName() {
        return disName;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDesignationId() {
        return designationId;
    }

    public String getAsmId() {
        return asmId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getSsoId() {
        return ssoId;
    }

    public String getUserDob() {
        return userDob;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getWeekOff() {
        return weekOff;
    }

    public String getUserDistrictId() {
        return userDistrictId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserTerritoryId() {
        return userTerritoryId;
    }

    public String getUserCountryId() {
        return userCountryId;
    }

    public String getUserActivationStatus() {
        return userActivationStatus;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getUserCityId() {
        return userCityId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCname() {
        return cname;
    }

    public String getSalesType() {
        return salesType;
    }

    public String getEmpCode() {
        return empCode;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public String getUserPinCode() {
        return userPinCode;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserState() {
        return userState;
    }

}

