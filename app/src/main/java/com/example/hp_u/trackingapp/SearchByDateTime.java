package com.example.hp_u.trackingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-u on 7/15/2017.
 */

public class SearchByDateTime extends Fragment {
    ListView L;
    EditText date,toTime,fromTime,mob;
    ImageButton search;
    List<Trackings> list=new ArrayList<Trackings>();
    CustomAdapter adp;
    Context ctx;
    double lt,ln,frndlt,frndln;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx=getActivity();
        LocationManager LOC=(LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        try {GrantPermission G3=new GrantPermission(ctx, Manifest.permission.INTERNET,5);
            GrantPermission G2=new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION,4);
            GrantPermission G4=new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION,3);

            LOC.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new SearchByDateTime.Loc_Update());
        }
        catch (Exception e)
        {GrantPermission G3=new GrantPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION,8);
            GrantPermission G2=new GrantPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION,9);
        }
        View root=inflater.inflate(R.layout.searchby_datetime,container,false);
        L=(ListView)root.findViewById(R.id.listView);
        mob=(EditText)root.findViewById(R.id.mb);
        date=(EditText) root.findViewById(R.id.date);
        toTime=(EditText)root.findViewById(R.id.toTime);
        fromTime=(EditText)root.findViewById(R.id.fromTime);
        search=(ImageButton)root.findViewById(R.id.searchLoc);
        L.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trackings T=list.get(position);
                frndlt = Double.parseDouble(T.getLatitude());
                frndln = Double.parseDouble(T.getLongitude());
                try{
                    displayOnMap(frndlt,frndln,lt,ln);
                }catch (Exception e){}
            }
        });

        search.setOnClickListener(new Search_loc(ctx));
        adp=new CustomAdapter(ctx,android.R.layout.simple_list_item_1,list);
        L.setAdapter(adp);
        adp.notifyDataSetChanged();
        return root;
    }

    class Loc_Update implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double lat=location.getLatitude();
            double lng=location.getLongitude();

            lt=lat;
            ln=lng;
            String s=lt+"-"+ln;
            // Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();
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

    private void displayOnMap(double lat, double lng,double lt,double ln){
        String uri="geo:"+lat+","+lng;
        Intent map=new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr="+lt+","+ln+"" + "&daddr="+frndlt+","+frndln));
        startActivity(map);
    }

    class Search_loc implements View.OnClickListener{
        Context ctx;

        public Search_loc(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            String qs = "http://" + ctx.getString(R.string.ip) + ":8080/TrackingApp/SearchByDateTimeJSON?tracking_date="+date.getText().toString()+"&fromTime="+fromTime.getText().toString()+"&toTime="+toTime.getText().toString()+"&mobileno="+mob.getText().toString();
            //Toast.makeText(ctx,qs,Toast.LENGTH_LONG).show();
            String url[]={qs};
            CallHttpRequest c=new CallHttpRequest(ctx,"SearchByDateTime",adp,list);
            c.execute(url);
        }
    }
}
