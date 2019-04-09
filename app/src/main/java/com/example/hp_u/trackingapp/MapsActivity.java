package com.example.hp_u.trackingapp;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Context ctx;
    String mb;
    double lt,ln,frndlt=0,frndln=0;

    public MapsActivity() {
    }

    public MapsActivity(String mb) {
        this.mb = mb;
    }

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ctx=this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        GrantPermission G = new GrantPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
        GrantPermission G1 = new GrantPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, 2);
        GrantPermission G6 = new GrantPermission(this, Manifest.permission.INTERNET, 6);

        LocationManager LOC=(LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        try {GrantPermission G3=new GrantPermission(ctx, Manifest.permission.INTERNET,5);
            GrantPermission G2=new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION,4);
            GrantPermission G4=new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION,3);


            LOC.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new Loc_Update(ctx));
        }
        catch (Exception e)
        {GrantPermission G3=new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION,8);
            GrantPermission G2=new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION,9);
        }
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

  /*  @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_maps,container,false);

        ctx=getActivity();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        GrantPermission G = new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION, 1);
        GrantPermission G1 = new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION, 2);
        GrantPermission G6 = new GrantPermission(ctx, Manifest.permission.INTERNET, 6);

        LocationManager LOC=(LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        try {GrantPermission G3=new GrantPermission(ctx, Manifest.permission.INTERNET,5);
            GrantPermission G2=new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION,4);
            GrantPermission G4=new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION,3);


            LOC.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new Loc_Update(ctx));
        }
        catch (Exception e)
        {GrantPermission G3=new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION,8);
            GrantPermission G2=new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION,9);
        }

        return v;
    }*/

    class Loc_Update implements LocationListener
    {
        Context ctx;

        public Loc_Update(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onLocationChanged(Location location) {
            double lat=location.getLatitude();
            double lng=location.getLongitude();

            lt=lat;
            ln=lng;
            String s=lt+"-"+ln;
            //Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();
            getAddress(lt,ln);

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

    void getAddress(double lat,double lng) {
        try {
            Geocoder GC = new Geocoder(ctx, Locale.getDefault());
            List<Address> A = GC.getFromLocation(lat, lng, 1);

            StringBuffer sb=new StringBuffer();
            if(A.size()>0) {
                Address add = A.get(0);
                for (int i = 0; i < add.getMaxAddressLineIndex(); i++) {
                    sb.append(add.getAddressLine(i)+" ");
                }
                String address=sb.toString().replace(" ","+");
                Calendar C = Calendar.getInstance();
                String cd = C.get(Calendar.YEAR) + "-" + (C.get(Calendar.MONTH) + 1) + "-" + C.get(Calendar.DATE);
                String ct = C.get(Calendar.HOUR) + ":" + C.get(Calendar.MINUTE) + ":" + C.get(Calendar.SECOND);
                String qs = "http://" + ctx.getString(R.string.ip) + ":8080/TrackingApp/TrackingSubmit?mobileno=" + mb + "&latitude=" + lat + "&longitude=" + lng + "&cd=" + cd + "&ct=" + ct + "&address="+address;

                CallHttpRequest tr=new CallHttpRequest(ctx,"Map");
                String url[]={qs};
                tr.execute(url);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(ctx,e+"",Toast.LENGTH_LONG).show();

        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lt, ln);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
