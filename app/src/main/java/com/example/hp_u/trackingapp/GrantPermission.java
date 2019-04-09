package com.example.hp_u.trackingapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Sandeep Sappal on 6/21/2016.
 */

public class GrantPermission implements ActivityCompat.OnRequestPermissionsResultCallback  {
    int MY_PERMISSION;
    String permission;

    Context ctx;
    boolean status=false;
    public GrantPermission(Context ctx,String permission,int MY_PERMISSION)
    {this.ctx=ctx;
        this.permission=permission;
        this.MY_PERMISSION=MY_PERMISSION;
        getPermission();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION) {
            doPermissionGrantedStuffs();
        }
    }




    public  void getPermission() {

        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(ctx, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            //requestReadPhoneStatePermission();
            ActivityCompat.requestPermissions((Activity)ctx, new String[]{permission},
                    MY_PERMISSION );
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }


    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        status=true;
    }
}
