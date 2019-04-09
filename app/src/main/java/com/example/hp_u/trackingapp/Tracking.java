package com.example.hp_u.trackingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Tracking extends Fragment {
    String mb = "";
    TextView T;
    Context ctx;

    public Tracking() {
        //this.mb = mb;
    }

    public Tracking(String mb) {
        this.mb = mb;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        View root = inflater.inflate(R.layout.activity_tracking, container, false);
        LocationManager LOC = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        T = (TextView) root.findViewById(R.id.trkMsg);
        //Intent I=getActivity().getIntent();
        //Bundle B=I.getExtras();
        // mb=B.getString("mobileno");

        try {
            GrantPermission G3 = new GrantPermission(ctx, Manifest.permission.INTERNET, 5);
            GrantPermission G2 = new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION, 4);
            GrantPermission G1 = new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION, 3);

            LOC.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new Loc_Update());
        } catch (Exception e) {
            GrantPermission G1 = new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION, 3);
            GrantPermission G2 = new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION, 4);
        }
        return root;
    }

    class Loc_Update implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            T.setText(lat + "," + lng);
            getAddress(lat, lng);
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

    void getAddress(double lat, double lng) {
        try {
            Geocoder GC = new Geocoder(ctx, Locale.getDefault());
            List<Address> A = GC.getFromLocation(lat, lng, 1);

            StringBuffer sb = new StringBuffer();
            if (A.size() > 0) {
                Address add = A.get(0);

                for (int i = 0; i <= add.getMaxAddressLineIndex(); i++) {
                    sb.append(add.getAddressLine(i) + " ");
                }
                String address = sb.toString().replace(" ", "+");

                Calendar C = Calendar.getInstance();
                String cd = C.get(Calendar.YEAR) + "-" + (C.get(Calendar.MONTH) + 1) + "-" + C.get(Calendar.DATE);
                String ct = C.get(Calendar.HOUR) + ":" + C.get(Calendar.MINUTE) + ":" + C.get(Calendar.SECOND);
                String qs = "http://" + ctx.getString(R.string.ip) + ":8080/TrackingApp/TrackingSubmit?mobileno=" + mb + "&latitude=" + lat + "&longitude=" + lng + "&cd=" + cd + "&ct=" + ct + "&address=" + address;

                CallHttpRequest tr = new CallHttpRequest(ctx, "Tracking");
                String url[] = {qs};
                tr.execute(url);
            }
        } catch (Exception e) {
            Toast.makeText(ctx, e + "", Toast.LENGTH_LONG).show();

        }
    }
}
