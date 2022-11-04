package com.malas.appsr.malasapp.BeanClasses;


import java.io.Serializable;

public class RouteBean implements Serializable {

    String distributor_id, routes_id, activation_status, route_name;

    public RouteBean(String distributor_id, String routes_id, String activation_status, String route_name) {
        this.distributor_id = distributor_id;
        this.routes_id = routes_id;
        this.activation_status = activation_status;
        this.route_name = route_name;
    }

    public RouteBean() {

    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    public String getRoutes_id() {
        return routes_id;
    }

    public void setRoutes_id(String routes_id) {
        this.routes_id = routes_id;
    }

    public String getActivation_status() {
        return activation_status;
    }

    public void setActivation_status(String activation_status) {
        this.activation_status = activation_status;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }
}
