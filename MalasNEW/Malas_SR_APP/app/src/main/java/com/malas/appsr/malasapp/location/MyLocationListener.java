package com.malas.appsr.malasapp.location;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {
        if (loc != null) {
            // Do something knowing the location changed by the distance you requested
//         methodThatDoesSomethingWithNewLocation(loc);
        }
    }
}