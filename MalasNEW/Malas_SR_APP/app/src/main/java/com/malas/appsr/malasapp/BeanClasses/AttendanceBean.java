package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 05-Dec-18.
 */

public class AttendanceBean {
    String userId;
    String attendanceType;
    String attendanceTypeId;
    String attendanceReason;
    String dsrStatus;
    String presAbsent;
    String attendanceDate;
    String createdAt;
    String keyId;
    String jointUserID;
    String jointUserDesig;


    public String getJointUserID() {
        return jointUserID;
    }

    public void setJointUserID(String jointUserID) {
        this.jointUserID = jointUserID;
    }

    public String getJointUserDesig() {
        return jointUserDesig;
    }

    public void setJointUserDesig(String jointUserDesig) {
        this.jointUserDesig = jointUserDesig;
    }


    public AttendanceBean(String userId, String attendanceType, String attendanceTypeId, String attendanceReason, String dsrStatus, String presAbsent, String attendanceDate, String createdAt, String keyId, String jointUserID, String jointUserDesig) {
        this.userId = userId;
        this.attendanceType = attendanceType;
        this.attendanceTypeId = attendanceTypeId;
        this.attendanceReason = attendanceReason;
        this.dsrStatus = dsrStatus;
        this.presAbsent = presAbsent;
        this.attendanceDate = attendanceDate;
        this.createdAt = createdAt;
        this.keyId = keyId;
        this.jointUserID = jointUserID;
        this.jointUserDesig = jointUserDesig;
    }

    public String getAttendanceTypeId() {
        return attendanceTypeId;
    }

    public void setAttendanceTypeId(String attendanceTypeId) {
        this.attendanceTypeId = attendanceTypeId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAttendanceReason() {
        return attendanceReason;
    }

    public void setAttendanceReason(String attendanceReason) {
        this.attendanceReason = attendanceReason;
    }

    public AttendanceBean() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getDsrStatus() {
        return dsrStatus;
    }

    public void setDsrStatus(String dsrStatus) {
        this.dsrStatus = dsrStatus;
    }

    public String getPresAbsent() {
        return presAbsent;
    }

    public void setPresAbsent(String presAbsent) {
        this.presAbsent = presAbsent;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
