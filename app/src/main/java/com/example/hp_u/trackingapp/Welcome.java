package com.example.hp_u.trackingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {
    SharedPreferences pos;
    String mssg="";
    String mob="";
    String pwd="";
    boolean loggedin_state=false;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ctx=this;

        pos=getApplicationContext().getSharedPreferences("userinfo",getApplicationContext().MODE_PRIVATE);
        loggedin_state=pos.getBoolean("loggedin",false);
        mob=pos.getString("mobileno","NO RECORD");
        pwd=pos.getString("pwd","NO RECORD");
        Handler H=new Handler();
        H.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loggedin_state){
                    try{
                        CallHttpRequest C=new CallHttpRequest(ctx,"CheckLogin",mssg);
                        String url[]={"http://"+Welcome.this.getString(R.string.ip)+":8080/TrackingApp/CheckLogin?user="+mob+"&password="+pwd};

                        C.execute(url);
                       // Toast.makeText(getApplicationContext(),loggedin_state+"",Toast.LENGTH_LONG).show();
                       // Intent i=new Intent(Welcome.this,NavigationActivity.class);
                       // startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    try {
                        //Toast.makeText(getApplicationContext(),loggedin_state+"", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Welcome.this, LoginActivity.class);
                        startActivity(i);
                    } catch (Exception e) {}
                }
               // Intent i = new Intent(Welcome.this, LoginActivity.class);
               // startActivity(i);

                finish();
                }
            },1500);
        }
    }

