package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Arwa on 21-Sep-18.
 */

public class ActivitySRBean {
    String act_id;
    String act_name;
    String act_start_date;
    String act_end_date;

    public ActivitySRBean(String act_id, String act_name, String act_start_date, String act_end_date) {
        this.act_id = act_id;
        this.act_name = act_name;
        this.act_start_date = act_start_date;
        this.act_end_date = act_end_date;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getAct_name() {
        return act_name;
    }

    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    public String getAct_start_date() {
        return act_start_date;
    }

    public void setAct_start_date(String act_start_date) {
        this.act_start_date = act_start_date;
    }

    public String getAct_end_date() {
        return act_end_date;
    }

    public void setAct_end_date(String act_end_date) {
        this.act_end_date = act_end_date;
    }
}
