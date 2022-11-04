package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

/**
 * Created by Krishna on 9/11/2016.
 */
public class DistributerBean implements Serializable {
    String distribution_id;
    String contact_person_name;
    String email_id;
    String mobile_no;
    String phone_no;
    String district_id;
    String city_id;
    String country_id;
    String firm_name;
    String tin;
    String cst_no;
    String address_firm;
    String address_godown;
    String constitution_firm;
    String bank_name;
    String branch;
    String account_no;
    String ifsc_code;
    String activation_status;
    String designation_id;
    String territory_id;
    String state_id;

    public DistributerBean() {

    }

    public String getTerritory_id() {
        return territory_id;
    }

    public void setTerritory_id(String territory_id) {
        this.territory_id = territory_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public DistributerBean(String distribution_id, String firm_name) {
        this.distribution_id = distribution_id;

        this.firm_name = firm_name;
    }

    public DistributerBean(String distribution_id, String contact_person_name, String email_id, String mobile_no, String phone_no, String district_id, String city_id, String country_id, String firm_name, String tin, String cst_no, String address_firm, String address_godown, String constitution_firm, String bank_name, String branch, String account_no, String ifsc_code, String activation_status, String designation_id, String territory_id, String state_id) {
        this.distribution_id = distribution_id;
        this.contact_person_name = contact_person_name;
        this.email_id = email_id;
        this.mobile_no = mobile_no;
        this.phone_no = phone_no;
        this.district_id = district_id;
        this.city_id = city_id;
        this.country_id = country_id;
        this.firm_name = firm_name;
        this.tin = tin;
        this.cst_no = cst_no;
        this.address_firm = address_firm;
        this.address_godown = address_godown;
        this.constitution_firm = constitution_firm;
        this.bank_name = bank_name;
        this.branch = branch;
        this.account_no = account_no;
        this.ifsc_code = ifsc_code;
        this.activation_status = activation_status;
        this.designation_id = designation_id;
        this.territory_id = territory_id;
        this.state_id= state_id;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getFirm_name() {
        return firm_name;
    }

    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getCst_no() {
        return cst_no;
    }

    public void setCst_no(String cst_no) {
        this.cst_no = cst_no;
    }

    public String getAddress_firm() {
        return address_firm;
    }

    public void setAddress_firm(String address_firm) {
        this.address_firm = address_firm;
    }

    public String getAddress_godown() {
        return address_godown;
    }

    public void setAddress_godown(String address_godown) {
        this.address_godown = address_godown;
    }

    public String getConstitution_firm() {
        return constitution_firm;
    }

    public void setConstitution_firm(String constitution_firm) {
        this.constitution_firm = constitution_firm;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getActivation_status() {
        return activation_status;
    }

    public void setActivation_status(String activation_status) {
        this.activation_status = activation_status;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(String designation_id) {
        this.designation_id = designation_id;
    }

    @Override
    public String toString() {
        return "DistributerBean{" +
                "distribution_id='" + distribution_id + '\'' +
                ", contact_person_name='" + contact_person_name + '\'' +
                ", email_id='" + email_id + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", district_id='" + district_id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", country_id='" + country_id + '\'' +
                ", firm_name='" + firm_name + '\'' +
                ", tin='" + tin + '\'' +
                ", cst_no='" + cst_no + '\'' +
                ", address_firm='" + address_firm + '\'' +
                ", address_godown='" + address_godown + '\'' +
                ", constitution_firm='" + constitution_firm + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", branch='" + branch + '\'' +
                ", account_no='" + account_no + '\'' +
                ", ifsc_code='" + ifsc_code + '\'' +
                ", activation_status='" + activation_status + '\'' +
                ", designation_id='" + designation_id + '\'' +
                '}';
    }
}
