package net.stitch.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 2015/10/23.
 */
public class MyLocationListener implements LocationListener {
    private GoogleMap mMap;
    LocationManager mlm;
    Activity mActivity;
    public  MyLocationListener(GoogleMap map, LocationManager lm, Activity activity){
        this.mMap = map;
        this.mlm = lm;
        this.mActivity = activity;
    }
    @Override
    public void onLocationChanged(Location location) {
        Double longitude = location.getLongitude();  //得到经度
        Double latitude = location.getLatitude();       //得到纬度
        LatLng HOME = new LatLng(longitude, latitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(HOME));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
