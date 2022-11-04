package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

/**
 * Created by Krishna on 9/11/2016.
 */
public class ReasonBean implements Serializable {
    String id;
    String reason;

    public ReasonBean(String id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public ReasonBean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
