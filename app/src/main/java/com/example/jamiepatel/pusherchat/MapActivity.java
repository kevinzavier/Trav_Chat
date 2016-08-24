package com.example.jamiepatel.pusherchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.security.Provider;
import java.util.List;


/**
 * Created by kevin on 8/16/16.
 */
public class MapActivity extends Activity{
    Button chat;
    Button addfriends;
    Button events;
    Button go;
    Button more;
    double mylongitude;
    double mylatitude;
    String provider;


    MapView mapView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //I use LocationManager to get the last saves location



        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        mylongitude = location.getLongitude();
        mylatitude = location.getLatitude();

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        chat = (Button) findViewById(R.id.button3);

        //over here i implement LocationListener to check for changes
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mylongitude = location.getLongitude();
                mylatitude = location.getLatitude();
            }
            public void onStatusChanged(String provider, int status, Bundle extras){}
            public void onProviderDisabled(String provider){}
            public void onProviderEnabled(String provider){}

        };
        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // When user clicks the map, animate to new camera location
                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(mylatitude, mylongitude)) // Sets the new camera position
                                .zoom(17) // Sets the zoom
                                .bearing(180) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 7000);
                    }
                });
            }
        });


        //Below is where I try to set the Camera
        //TODO this part is not working, the camera is still pointed to New York (that is what I set it to in the xml file)
        //LatLng myLatLng = new LatLng(latitude, longitude);
        //CameraPosition.Builder cam = new CameraPosition.Builder();
        //cam.target(myLatLng);





        Toast.makeText(MapActivity.this, String.valueOf(mylatitude) + "---" + String.valueOf(mylongitude), Toast.LENGTH_LONG).show();
        Log.i("THE LAT IS HERE", String.valueOf(mylatitude));
        Log.i("THE LONG IS HERE", String.valueOf(mylongitude));



    }

    public void onBottonTap(View v){
        if(v.getId()==R.id.button3){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
