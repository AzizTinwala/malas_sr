package com.malas.appsr.malasapp.BeanClasses;


import java.io.Serializable;

public class MonthBean implements Serializable {

    String id;
    String name;

    public MonthBean(String name, String id) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
