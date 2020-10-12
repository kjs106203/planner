package com.senier_project.planner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RefreshThread extends Thread {

    RouteActivity m_parent;
    private LocationManager locationManager;

    double longitude;
    double latitude;


    public RefreshThread(RouteActivity parent) {

        m_parent = parent;

        //위치정보 갱신
        LocationListener locationListener = location -> {
        };
        locationManager = (LocationManager) m_parent.getSystemService(Context.LOCATION_SERVICE);

        try {
            if (ContextCompat.checkSelfPermission(m_parent.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(m_parent, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PERMISSION", "PERMISSION ERROR");
        }


    }

    @Override
    public void run() {
        while (true) {
            try {
                if (ContextCompat.checkSelfPermission(m_parent.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(m_parent, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }

                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                if (loc != null) {
                    longitude = loc.getLongitude();
                    latitude = loc.getLatitude();

                    m_parent.longitude = longitude;
                    m_parent.latitude = latitude;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PERMISSION", "PERMISSION ERROR");
            }


        }


    }
}


