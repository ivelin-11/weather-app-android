package com.nbu.weather_app_main_f104930;

import android.annotation.SuppressLint;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationController {
    @SuppressLint("MissingPermission")
    public static void requestLocationUpdate(LocationManager locationManager,LocationListener locationListener){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                Constants.MIN_TIME,
                Constants.MIN_DISTANCE,
                locationListener);
    }

    public static void removeUpdates(LocationManager locationManager,LocationListener locationListener){
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
