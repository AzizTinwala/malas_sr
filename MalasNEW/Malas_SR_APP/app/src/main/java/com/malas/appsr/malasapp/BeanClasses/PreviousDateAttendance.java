package com.malas.appsr.malasapp.BeanClasses;

import java.io.Serializable;

/**
 * Created by Arwa on 29-Jan-19.
 */

public class PreviousDateAttendance implements Serializable {
    String date;
    String dsr_status;
    String attendance;
    String type;
    String attndanceId;
    String typeId;
    String reason;

    public PreviousDateAttendance(String attndanceId, String type, String typeId, String attendance, String dsr_status, String date) {
        this.date = date;
        this.dsr_status = dsr_status;
        this.attendance = attendance;
        this.type = type;
        this.attndanceId = attndanceId;
        this.typeId = typeId;

    }

    public String getAttndanceId() {
        return attndanceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAttndanceId(String attndanceId) {
        this.attndanceId = attndanceId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public PreviousDateAttendance(String date) {
        this.date = date;
    }

    public PreviousDateAttendance(String date, String dsr_status,String attendance,String type,String reason) {
        this.date = date;
        this.dsr_status = dsr_status;
        this.attendance = attendance;
        this.type = type;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getDsr_status() {
        return dsr_status;
    }

    public void setDsr_status(String dsr_status) {
        this.dsr_status = dsr_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
