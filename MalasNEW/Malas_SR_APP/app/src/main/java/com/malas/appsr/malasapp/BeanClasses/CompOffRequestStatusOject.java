package com.malas.appsr.malasapp.BeanClasses;

public class CompOffRequestStatusOject {

    private String requestId;
    private String fdate;
    private String tdate;
    private String sendDate;
    private String leave_days;
    private int status;
    private String lreason;
    private String remaining_days;

    public CompOffRequestStatusOject(String requestId, String fdate, String tdate, String sendDate, String leave_days, int status, String lreason, String remaining_days) {
        this.requestId = requestId;
        this.fdate = fdate;
        this.tdate = tdate;
        this.leave_days = leave_days;
        this.sendDate = sendDate;
        this.status = status;
        this.lreason = lreason;
        this.remaining_days = remaining_days;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getLeave_days() {
        return leave_days;
    }

    public void setLeave_days(String leave_days) {
        this.leave_days = leave_days;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLreason() {
        return lreason;
    }

    public void setLreason(String lreason) {
        this.lreason = lreason;
    }

    public String getRemaining_days() {
        return remaining_days;
    }

    public void setRemaining_days(String remaining_days) {
        this.remaining_days = remaining_days;
    }
}
