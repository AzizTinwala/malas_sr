package com.malas.appsr.malasapp.BeanClasses;

public class TourPlanBean {

    String id, date,workType, teamList, townFrom, townTo, distributor, route, updatedBy, updatedAt;
    int workTypeID, teamListID, townFromID, townToID, distributorID, routeID, updatedByID;

    public TourPlanBean(){}

    public TourPlanBean(String id, String date, String workType, String teamList, String townFrom, String townTo, String distributor, String route, String updatedBy, String updatedAt, int workTypeID, int teamListID, int townFromID, int townToID, int distributorID, int routeID, int updatedByID) {
        this.id = id;
        this.date = date;
        this.workType = workType;
        this.teamList = teamList;
        this.townFrom = townFrom;
        this.townTo = townTo;
        this.distributor = distributor;
        this.route = route;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        this.workTypeID = workTypeID;
        this.teamListID = teamListID;
        this.townFromID = townFromID;
        this.townToID = townToID;
        this.distributorID = distributorID;
        this.routeID = routeID;
        this.updatedByID = updatedByID;
    }

    public TourPlanBean(String date, int workTypeID, int teamListID, int townFromID, int townToID, int distributorID, int routeID, int updatedByID) {
        this.date = date;
        this.workTypeID = workTypeID;
        this.teamListID = teamListID;
        this.townFromID = townFromID;
        this.townToID = townToID;
        this.distributorID = distributorID;
        this.routeID = routeID;
        this.updatedByID = updatedByID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getTeamList() {
        return teamList;
    }

    public void setTeamList(String teamList) {
        this.teamList = teamList;
    }

    public String getTownFrom() {
        return townFrom;
    }

    public void setTownFrom(String townFrom) {
        this.townFrom = townFrom;
    }

    public String getTownTo() {
        return townTo;
    }

    public void setTownTo(String townTo) {
        this.townTo = townTo;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getWorkTypeID() {
        return workTypeID;
    }

    public void setWorkTypeID(int workTypeID) {
        this.workTypeID = workTypeID;
    }

    public int getTeamListID() {
        return teamListID;
    }

    public void setTeamListID(int teamListID) {
        this.teamListID = teamListID;
    }

    public int getTownFromID() {
        return townFromID;
    }

    public void setTownFromID(int townFromID) {
        this.townFromID = townFromID;
    }

    public int getTownToID() {
        return townToID;
    }

    public void setTownToID(int townToID) {
        this.townToID = townToID;
    }

    public int getDistributorID() {
        return distributorID;
    }

    public void setDistributorID(int distributorID) {
        this.distributorID = distributorID;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public int getUpdatedByID() {
        return updatedByID;
    }

    public void setUpdatedByID(int updatedByID) {
        this.updatedByID = updatedByID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
