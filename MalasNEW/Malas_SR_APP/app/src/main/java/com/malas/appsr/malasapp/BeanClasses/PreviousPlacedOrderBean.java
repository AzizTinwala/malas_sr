package com.malas.appsr.malasapp.BeanClasses;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Admin on 04-Feb-18.
 */

public class PreviousPlacedOrderBean {

    String datetime;
    String placeordertime;
    ArrayList<PreviousPlaceorderArraylistBean> previousPlaceorderArraylistBeans;

    public ArrayList<PreviousPlaceorderArraylistBean> getPreviousPlaceorderArraylistBeans() {
        return previousPlaceorderArraylistBeans;
    }

    public void setPreviousPlaceorderArraylistBeans(ArrayList<PreviousPlaceorderArraylistBean> previousPlaceorderArraylistBeans) {
        this.previousPlaceorderArraylistBeans = previousPlaceorderArraylistBeans;
    }

    public PreviousPlacedOrderBean(String datetime, String placeordertime, ArrayList<PreviousPlaceorderArraylistBean> previousPlaceorderArraylistBeans) {
        this.datetime = datetime;
        this.placeordertime = placeordertime;
        this.previousPlaceorderArraylistBeans = previousPlaceorderArraylistBeans;
    }

    public String getPlaceordertime() {
        return placeordertime;
    }

    public void setPlaceordertime(String placeordertime) {
        this.placeordertime = placeordertime;
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public static class OrderByTimeStampComparator implements Comparator<PreviousPlacedOrderBean> {
        @Override
        public int compare(PreviousPlacedOrderBean o1, PreviousPlacedOrderBean o2) {
            if (Long.parseLong(o1.getPlaceordertime()) > Long.parseLong(o2.getPlaceordertime())) {
                return 1;
            } else if (Long.parseLong(o1.getPlaceordertime()) < Long.parseLong(o2.getPlaceordertime())) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
