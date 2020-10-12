package com.senier_project.planner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import com.google.android.gms.location.LocationRequest;
import com.senier_project.planner.Activity.MainActivity;

public class RouteActivity extends AppCompatActivity {


    double longitude = 0;
    double latitude = 0;

    ListView listView;

    TextView textView1;

    LocationManager locationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);


        //현재 위도 경도값 받기
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        //가장 최근 위도 경도값 가져오기
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (loc != null) {
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        }

        //도착지 관련 위도 경도
        Intent intent = getIntent();
        String x = intent.getExtras().getString("X");
        String y = intent.getExtras().getString("Y");



        RefreshThread refreshThread = new RefreshThread(this);
        refreshThread.start();


        textView1 = findViewById(R.id.test1);

        textView1.setText("long" + longitude);

    }

}