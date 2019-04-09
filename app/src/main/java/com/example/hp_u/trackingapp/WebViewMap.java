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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WebViewMap extends Fragment {
    Context ctx;
    String mb;
    double lt,ln,frndlt=0,frndln=0;

    public WebViewMap() {
    }

    public WebViewMap(String mb) {
        this.mb = mb;
    }

   /* public WebViewMap(double frndlt, double frndln) {
        this.frndlt = frndlt;
        this.frndln = frndln;
    }*/

    WebView myWebView;
    String mapPath = "https://www.google.co.in/maps/@"+lt+","+ln+"18z";
   // String frndPath="http://maps.google.com/maps?" + "saddr="+lt+","+ln+"+&daddr="+frndlt+","+frndln;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx=getActivity();
        View v=inflater.inflate(R.layout.activity_web_view_map,container,false);
        GrantPermission G=new GrantPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION,1);
        GrantPermission G1=new GrantPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION,2);
        GrantPermission G6=new GrantPermission(getActivity(),Manifest.permission.INTERNET,6);


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


        myWebView = (WebView)v.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        myWebView.getSettings().setGeolocationEnabled(true);

        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        DisplayMetrics m=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(m);
        int density=m.densityDpi;

        if(density==120){
            myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }
        else if(density==160){
            myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        else if(density==240){
            myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });

       /* if(!(frndln==0 && frndlt==0)){
            myWebView.loadUrl(frndPath);
        }else{
            myWebView.loadUrl(mapPath);
        }*/
        myWebView.loadUrl(mapPath);
        return v;
    }
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
    void getAddress(double lat,double lng)
    {
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
}
