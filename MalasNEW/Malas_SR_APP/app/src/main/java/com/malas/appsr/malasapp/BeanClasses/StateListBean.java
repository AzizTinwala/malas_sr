package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by xyz on 9/8/2016.
 */
public class StateListBean {

    String StateId, stateName, countryId;

    public StateListBean(String stateId, String stateName, String countryId) {
        StateId = stateId;
        this.stateName = stateName;
        this.countryId = countryId;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
