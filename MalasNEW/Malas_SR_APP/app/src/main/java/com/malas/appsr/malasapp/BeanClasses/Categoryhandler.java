package com.malas.appsr.malasapp.BeanClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categoryhandler implements Parcelable {

    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_status")
    @Expose
    private String categoryStatus;
    public final static Creator<Categoryhandler> CREATOR = new Creator<Categoryhandler>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Categoryhandler createFromParcel(Parcel in) {
            Categoryhandler instance = new Categoryhandler();
            instance.outletId = ((String) in.readValue((String.class.getClassLoader())));
            instance.categoryId = ((String) in.readValue((String.class.getClassLoader())));
            instance.categoryStatus = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Categoryhandler[] newArray(int size) {
            return (new Categoryhandler[size]);
        }

    };

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(outletId);
        dest.writeValue(categoryId);
        dest.writeValue(categoryStatus);
    }

    public int describeContents() {
        return 0;
    }

}