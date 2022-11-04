package com.malas.appsr.malasapp;

/**
 * Created by Arwa on 05-Dec-18.
 */

public class AttendanceReasonBean {
    String attendanceReason;
    String attendanceType;
    private String attendanceId;
    private String attendanceMessage;

    String getAttendanceMessage() {
        return attendanceMessage;
    }

    public void setAttendanceMessage(String attendanceMessage) {
        this.attendanceMessage = attendanceMessage;
    }

    public AttendanceReasonBean() {
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public AttendanceReasonBean(String attendanceId,String attendanceReason, String attendanceType) {
        this.attendanceId = attendanceId;
        this.attendanceReason = attendanceReason;
        this.attendanceType = attendanceType;
    }

    public String getAttendanceReason() {
        return attendanceReason;
    }

    public void setAttendanceReason(String attendanceReason) {
        this.attendanceReason = attendanceReason;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }
}
