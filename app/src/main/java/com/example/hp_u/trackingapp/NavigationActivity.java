package com.example.hp_u.trackingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView img;
    TextView fullname,email;
    String firstname,lastname,emailid,name,mob="";
    SharedPreferences pos;
    Fragment F;
    SubActionButton button_profile,button_phonesearch,button_datetimesearch,button_mylocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        pos=this.getSharedPreferences("userinfo", this.MODE_PRIVATE);
        mob=pos.getString("mobileno","NO RECORD");
        firstname=pos.getString("firstname","NO RECORD");
        lastname=pos.getString("lastname","NO RECORD");
        emailid=pos.getString("emailid","NO RECORD");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Friends Tracker");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Logged in as "+name, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView profile = new ImageView(this);
        profile.setImageResource(R.drawable.myprofile);

        ImageView phonesearch = new ImageView(this);
        phonesearch.setImageResource(R.drawable.ic_phone_android_black_24dp);

        ImageView datetimesearch = new ImageView(this);
        datetimesearch.setImageResource(R.drawable.ic_date_range_black_24dp);

        ImageView mylocation = new ImageView(this);
        mylocation.setImageResource(R.drawable.ic_my_location_black_24dp);

        button_profile = itemBuilder.setContentView(profile).build();
        button_phonesearch = itemBuilder.setContentView(phonesearch).build();
        button_datetimesearch = itemBuilder.setContentView(datetimesearch).build();
        button_mylocation = itemBuilder.setContentView(mylocation).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button_profile)
                .addSubActionView(button_datetimesearch)
                .addSubActionView(button_phonesearch)
                .addSubActionView(button_mylocation)
                .attachTo(fab)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navigationView.setNavigationItemSelectedListener(this);

        View v=navigationView.inflateHeaderView(R.layout.nav_header_navigation);
        img=(ImageView)v.findViewById(R.id.imageView);
        fullname=(TextView)v.findViewById(R.id.full_name);
        email=(TextView)v.findViewById(R.id.email_id);


       // Intent I=this.getIntent();
       // Bundle B=I.getExtras();

        //mob=B.getString("mobileno");

       // firstname=B.getString("firstname");
        //lastname=B.getString("lastname");
       // emailid=B.getString("email");
        name=firstname+" "+lastname;

        fullname.setText(name);
        email.setText(emailid);

        //Toast.makeText(this,firstname+" "+lastname+" "+emailid,Toast.LENGTH_LONG).show();

        String q="http://"+ this.getString(R.string.ip) +":8080/TrackingApp/pic/"+mob+".jpg";
        String qurl[]={q};
        CallHttpRequest call=new CallHttpRequest(this,"FetchPic",img);
        call.execute(qurl);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F=new MyProfile(firstname,lastname,mob,emailid,img);
                android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content,F);
                ft.commit();
            }
        });

        button_phonesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F=new SearchByMobile();
                android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content,F);
                ft.commit();
            }
        });

        button_datetimesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                F=new SearchByDateTime();
                android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content,F);
                ft.commit();
            }
        });

        button_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F=new WebViewMap(mob);
                android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content,F);
                ft.commit();
            }
        });

        if (id == R.id.nav_tracking) {
            F= new WebViewMap(mob);
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,F);
            ft.commit();

        } else if (id == R.id.nav_searchbymob) {
            F=new SearchByMobile();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,F);
            ft.commit();

        } else if (id == R.id.nav_searchbydatetimemob) {
            F=new SearchByDateTime();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,F);
            ft.commit();

        } else if (id == R.id.nav_profile) {
            F=new MyProfile(firstname,lastname,mob,emailid,img);
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content,F);
            ft.commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){
            pos=this.getSharedPreferences("userinfo",this.MODE_PRIVATE);
            SharedPreferences.Editor edit=pos.edit();
            edit.clear();
            edit.commit();

            Intent i=new Intent(this,LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
