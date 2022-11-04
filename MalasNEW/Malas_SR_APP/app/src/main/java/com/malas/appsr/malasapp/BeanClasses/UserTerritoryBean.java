package com.malas.appsr.malasapp.BeanClasses;

public class UserTerritoryBean {
    private String territoryId;
    private String territoryName;
    private String territoryState;

    public UserTerritoryBean(String territoryId, String territoryName,String territoryState) {
        this.territoryId = territoryId;
        this.territoryName = territoryName;
        this.territoryState = territoryState;
    }

    public String getTerritoryState() {
        return territoryState;
    }

    public void setTerritoryState(String territoryState) {
        this.territoryState = territoryState;
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
}
