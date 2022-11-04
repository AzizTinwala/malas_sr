package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/8/2016.
 */
public class TerritoryListBean {

    String territoryId, territoryName, status, state_id;

    public TerritoryListBean(String territoryId, String territoryName, String status, String state_id) {
        this.territoryId = territoryId;
        this.territoryName = territoryName;
        this.state_id = state_id;
        this.status = status;
    }

    public String getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(String territoryId) {
        this.territoryId = territoryId;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }
}
