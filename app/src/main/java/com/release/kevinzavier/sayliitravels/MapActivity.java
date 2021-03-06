package com.release.kevinzavier.sayliitravels;

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
import android.widget.Toast;


import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


/**
 * Created by kevin on 8/16/16.
 */
public class MapActivity extends Activity{
    String myname ="";
    Button chat;
    Button addfriends;
    Button pin;
    Button go;
    Button more;
    double mylongitude;
    double mylatitude;
    String provider;
    boolean first = true;
    int zoomfactor;
    double markerLat;
    double markerLong;
    MapboxMap myMapboxMap;





    MapView mapView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //I use LocationManager to get the last saves location

        Toast.makeText(MapActivity.this, "Tap the screen to go to your location", Toast.LENGTH_SHORT).show();

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if(location == null){
            Toast.makeText(MapActivity.this, "please reopen the app, and turn on location", Toast.LENGTH_LONG).show();
            mylatitude = 34.233919;
            mylongitude = -118.250664;
            zoomfactor = 7;
        }
        else {
            mylongitude = location.getLongitude();
            mylatitude = location.getLatitude();
            zoomfactor = 11;
        }

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        chat = (Button) findViewById(R.id.button4);
        chat.setBackgroundResource(R.drawable.ic_menu_chat);
        addfriends = (Button) findViewById(R.id.button3);
        addfriends.setBackgroundResource(R.drawable.ic_3);
        go = (Button) findViewById(R.id.button5);
        go.setBackgroundResource(R.drawable.ic_menu_go);
        pin = (Button) findViewById(R.id.button7);
        pin.setBackgroundResource(R.drawable.ic_menu_addpin);
        more = (Button) findViewById(R.id.button6);
        more.setBackgroundResource(R.drawable.ic_menu_menu);


        //over here i implement LocationListener to check for changes
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mylongitude = location.getLongitude();
                mylatitude = location.getLatitude();
                zoomfactor = 11;
            }
            public void onStatusChanged(String provider, int status, Bundle extras){}
            public void onProviderDisabled(String provider){}
            public void onProviderEnabled(String provider){}

        };
        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                //so we can access it outside
                myMapboxMap = mapboxMap;

                // When user clicks the map, animate to new camera location
                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        if(first) {
                            //makes the camera move
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(mylatitude, mylongitude)) // Sets the new camera position
                                    .zoom(zoomfactor) // Sets the zoom
                                    .bearing(180) // Rotate the camera
                                    .tilt(0) // Set the camera tilt
                                    .build(); // Creates a CameraPosition from the builder

                            mapboxMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(position), 7000);
                        }
                        //so it doesn't do this again on clicks
                        first = false;
                    }
                });
                mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {
                        //HERE
                        Log.i("", "BRUHHHHHHHHHHHHHH");
                        Intent i = new Intent(MapActivity.this, PopMenu.class);
                        startActivityForResult(i, 0);
                        markerLat = point.getLatitude();
                        markerLong = point.getLongitude();
                    }
                });
                Bundle extras = getIntent().getExtras();
                if(extras != null) {
                    myname = extras.getString("name");
                }
                mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(mylatitude, mylongitude))
                        .title(myname)
                        .snippet("You are currently here"));
            }

        });

        MapboxGeocoder client = new MapboxGeocoder.Builder()
                .setAccessToken("pk.eyJ1Ijoia2V2aW56YXZpZXIiLCJhIjoiY2lyZHRydWJpMDFqdmdobTN1OHVzZHZsNSJ9.1kycJM_bgrwAsqHqznpuZA")
                .setLocation("The White House")
                .build();




        Log.i("THE LAT IS HERE", String.valueOf(mylatitude));
        Log.i("THE LONG IS HERE", String.valueOf(mylongitude));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("", "INTERESTING");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bundle basket = data.getExtras();
            String info = basket.getString("info");
            myMapboxMap.addMarker(new MarkerViewOptions()
                    .position(new LatLng(markerLat, markerLong))
                    .title(myname)
                    .snippet(info));
            Toast.makeText(MapActivity.this, "Added a new Marker", Toast.LENGTH_LONG).show();
        }
        else{

        }
    }

    public void onBottonTap(View v){
        if(v.getId()==R.id.button4){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.button3){
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
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
