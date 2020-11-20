package com.yashraj.bloodonation.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.yashraj.bloodonation.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = "MainActivity";
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    SupportMapFragment mapFragment;
    private Boolean mLocationgranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getPermission();
        if (mLocationgranted) {
            getLocation();
            initMap();
        }
    }

    private void moveCamera(LatLng latLng,float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        mMap.addMarker(new MarkerOptions().position(latLng));
    }
    private void getLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationgranted){
                Task location=fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentlocation=(Location)task.getResult();
                            if (currentlocation != null) {
                                moveCamera(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()),18);
                            }
                        }else{
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getPermission(){
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationgranted=true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE);
        }

    }

    public void initMap(){
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationgranted=false;
        switch (requestCode){
            case REQUEST_CODE:{
                if(grantResults.length>0){
                    mLocationgranted=true;
                    initMap();
                    for (int i = 0; i <grantResults.length ; i++) {
                        if( PackageManager.PERMISSION_GRANTED == grantResults[i]){
                            mLocationgranted=false;
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
    }
}