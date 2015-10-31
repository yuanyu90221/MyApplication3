package net.stitch.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends AppCompatActivity {
    private GoogleMap mMap;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        setUpMapIfNeeded();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(false);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("sorry~~no service there");
            return;
        }
        double lat, lng;
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locLis = new MyLocationListener(mMap, lm, this);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLis);

        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 設定定位資訊由 GPS提供
        lat = location.getLatitude();  // 取得經度
        lng = location.getLongitude(); // 取得緯度
        LatLng HOME = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(HOME));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(18);
        mMap.animateCamera(zoom);

        MarkerOptions markerOpt = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.stitch));
        markerOpt.position(new LatLng(lat, lng));
        markerOpt.draggable(true);
        mMap.addMarker(markerOpt);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(GoogleMapActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        // Initialize map options. For example:
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

}
