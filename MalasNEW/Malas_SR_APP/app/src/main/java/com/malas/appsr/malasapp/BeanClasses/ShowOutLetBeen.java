package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

public class ShowOutLetBeen implements Serializable {
String uniqueIdDB;
    String category_handlar;
    String contact_person_name;
    String outlet_email;
    String type_appointment;
    String activation_status;
    String country_id;
    String state_id;
    String district_id;
    String territory_id;
    String city_id;
    String supply_chain;
    String contact_number;
    String outlet_name;
    String addedby;
    String address;
    String residential_address;
    String outlet_id;
    String mobile_no;
    String distribution_id;
    String route_id;
    String distribution_name;
    String route_name;


    public ShowOutLetBeen(String category_handlar, String contact_person_name, String outlet_email, String type_appointment, String activation_status, String country_id, String supply_chain, String contact_number, String city_id, String outlet_name, String addedby, String address, String residential_address, String outlet_id, String mobile_no, String distribution_id, String route_id, String distribution_name, String route_name, String state_id, String district_id, String territory_id) {
        this.category_handlar = category_handlar;
        this.contact_person_name = contact_person_name;
        this.outlet_email = outlet_email;
        this.type_appointment = type_appointment;
        this.activation_status = activation_status;
        this.country_id = country_id;
        this.supply_chain = supply_chain;
        this.contact_number = contact_number;
        this.city_id = city_id;
        this.outlet_name = outlet_name;
        this.addedby = addedby;
        this.address = address;
        this.residential_address = residential_address;
        this.outlet_id = outlet_id;
        this.mobile_no = mobile_no;
        this.distribution_id = distribution_id;
        this.route_id = route_id;
        this.distribution_name = distribution_name;
        this.route_name = route_name;
        this.state_id = state_id;
        this.district_id = district_id;
        this.territory_id = territory_id;
    }

    public ShowOutLetBeen() {

    }

    public String getUniqueIdDB() {
        return uniqueIdDB;
    }

    public void setUniqueIdDB(String uniqueIdDB) {
        this.uniqueIdDB = uniqueIdDB;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getDistribution_name() {
        return distribution_name;
    }

    public void setDistribution_name(String distribution_name) {
        this.distribution_name = distribution_name;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }

    public String getCategory_handlar() {
        return category_handlar;
    }

    public void setCategory_handlar(String category_handlar) {
        this.category_handlar = category_handlar;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getOutlet_email() {
        return outlet_email;
    }

    public void setOutlet_email(String outlet_email) {
        this.outlet_email = outlet_email;
    }

    public String getType_appointment() {
        return type_appointment;
    }

    public void setType_appointment(String type_appointment) {
        this.type_appointment = type_appointment;
    }

    public String getActivation_status() {
        return activation_status;
    }

    public void setActivation_status(String activation_status) {
        this.activation_status = activation_status;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getSupply_chain() {
        return supply_chain;
    }

    public void setSupply_chain(String supply_chain) {
        this.supply_chain = supply_chain;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getAddedby() {
        return addedby;
    }

    public void setAddedby(String addedby) {
        this.addedby = addedby;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResidential_address() {
        return residential_address;
    }

    public void setResidential_address(String residential_address) {
        this.residential_address = residential_address;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getTerritory_id() {
        return territory_id;
    }

    public void setTerritory_id(String territory_id) {
        this.territory_id = territory_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }
}