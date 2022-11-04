package com.malas.appsr.malasapp.BeanClasses;

public class LeaveRequestStatusOject {
    private String startDate;
    private String date;
    private String days;
    private int status;
    private String type;
    private String dtype;
    private String dtype1;
    private String rejectReason;
    private String endDate;
    private String lreason;
    private String lvid;

    public LeaveRequestStatusOject(String startDate, String endDate,String date, String days, int status, String type, String rejectReason, String lreason) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.date = date;
        this.days = days;
        this.status = status;
        this.type = type;
        this.rejectReason = rejectReason;
        this.lreason = lreason;
    }

    public LeaveRequestStatusOject(String startDate, String date, String days, int status, String type, String endDate) {
        this.startDate = startDate;
        this.date = date;
        this.days = days;
        this.status = status;
        this.type = type;
        this.endDate = endDate;
    }  public LeaveRequestStatusOject(String lvid,String startDate, String date, String days, int status, String type, String endDate,String dtype1,String dtype) {
        this.startDate = startDate;
        this.lvid = lvid;
        this.date = date;
        this.days = days;
        this.status = status;
        this.dtype1 = dtype1;
        this.dtype = dtype;
        this.type = type;
        this.endDate = endDate;
    }

    public String getLvid() {
        return lvid;
    }

    public void setLvid(String lvid) {
        this.lvid = lvid;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDtype1() {
        return dtype1;
    }

    public void setDtype1(String dtype1) {
        this.dtype1 = dtype1;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLreason() {
        return lreason;
    }

    public void setLreason(String lreason) {
        this.lreason = lreason;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String endDate) {
        this.date = date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
