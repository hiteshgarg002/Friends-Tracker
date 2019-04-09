package com.example.hp_u.trackingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-u on 7/7/2017.
 */

public class CallHttpRequest extends AsyncTask<String, Void, String> {
    Context ctx;
    TextView T;
    String activity, fname, lname, email, message, mssg, mob, password;
    CustomAdapter adp;
    ImageView img;
    Bitmap bm;
    List<Trackings> list = new ArrayList<Trackings>();

    public CallHttpRequest(Context ctx, TextView t, String activity, String message) {
        this.ctx = ctx;
        T = t;
        this.activity = activity;
        this.message = message;
    }

    public CallHttpRequest(Context ctx, String activity, ImageView img) {
        this.ctx = ctx;
        this.activity = activity;
        this.img = img;
    }

    public CallHttpRequest(Context ctx, String activity, CustomAdapter adp, List<Trackings> list) {
        this.ctx = ctx;
        this.activity = activity;
        this.adp = adp;
        this.list = list;
    }

    File fileUpload;

    CallHttpRequest(Context ctx, File fileUpload, String activity) {
        this.ctx = ctx;
        this.fileUpload = fileUpload;
        this.activity = activity;
    }

    CallHttpRequest(Context ctx, TextView T, String activity) {
        this.ctx = ctx;
        this.T = T;
        this.activity = activity;

    }

    CallHttpRequest(Context ctx, String activity) {
        this.ctx = ctx;
        this.activity = activity;
    }

    public CallHttpRequest(Context ctx, String activity, String mssg) {
        this.ctx = ctx;
        this.activity = activity;
        this.mssg = mssg;
    }

    @Override
    protected String doInBackground(String... url) {
        if (activity.equals("FileUpload")) {
            String res = fileUpload(url[0], fileUpload);
            return res;
        } else if (activity.equals("FetchPic")) {
            FetchPic(url[0]);
            return "PicLoaded";
        } else {
            String res = callRequest(url[0]);
            return res;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (activity.equals("Registration")) {
            if (s.equals("1"))
                Toast.makeText(ctx, "Registered Successfully", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ctx, "Something went wrong", Toast.LENGTH_LONG).show();
        } else if (activity.equals("CheckLogin")) {

            if (s.equals("0")) {
                //mssg="Invalid ID/Password";
                Toast.makeText(ctx, "Invalid ID/Password", Toast.LENGTH_LONG).show();
            } else if (s.equals("-1")) {
                Toast.makeText(ctx, "Error on Server", Toast.LENGTH_LONG).show();
            } else {
                //T.setText("Valid User ID "+s);
                Intent I = new Intent(ctx, NavigationActivity.class);
                // I.putExtra("mobileno",s);
                ctx.startActivity(I);
            }
        } else if (activity.equals("Tracking")) {
        } else if (activity.equals("Map")) {
        } else if (activity.equals("SearchByMobile")) {
            try {
                list.clear();
                JSONArray records = new JSONArray(s);
                for (int i = 0; i < records.length(); i++) {
                    JSONObject rec = records.optJSONObject(i);
                    Trackings T = new Trackings();
                    T.setTrackingId(Integer.parseInt(rec.optString("ID")));
                    T.setMobileno(rec.optString("MOBILENO"));
                    T.setLatitude(rec.optString("LATITUDE"));
                    T.setLongitude(rec.optString("LONGITUDE"));
                    T.setTracking_time(rec.optString("TRACKINGTIME"));
                    T.setTracking_date(rec.optString("TRACKINGDATE"));
                    T.setAddress(rec.optString("ADDRESS"));
                    list.add(T);
                }
                adp.notifyDataSetChanged();
            } catch (Exception e) {
            }
        } else if (activity.equals("SearchByDateTime")) {
            try {
                list.clear();
                JSONArray records = new JSONArray(s);
                for (int i = 0; i < records.length(); i++) {
                    JSONObject rec = records.optJSONObject(i);
                    Trackings T = new Trackings();
                    T.setTrackingId(Integer.parseInt(rec.optString("ID")));
                    T.setMobileno(rec.optString("MOBILENO"));
                    T.setLatitude(rec.optString("LATITUDE"));
                    T.setLongitude(rec.optString("LONGITUDE"));
                    T.setTracking_time(rec.optString("TRACKING_TIME"));
                    T.setTracking_date(rec.optString("TRACKING_DATE"));
                    T.setAddress(rec.optString("ADDRESS"));
                    list.add(T);
                }
                adp.notifyDataSetChanged();
            } catch (Exception e) {
            }
        } else if (activity.equals("GetUserInfo")) {
            try {

                JSONArray records = new JSONArray(s);
                for (int i = 0; i < records.length(); i++) {
                    JSONObject rec = records.optJSONObject(i);

                    fname = rec.optString("FIRSTNAME");
                    lname = rec.optString("LASTNAME");
                    email = rec.optString("EMAILID");
                    mob = rec.optString("MOBILENO");
                    password = rec.optString("PASSWORD");
                    Toast.makeText(ctx, "Welcome " + fname, Toast.LENGTH_LONG).show();
                    getUserInfo(fname, lname, email, password);
                }
            } catch (Exception e) {
            }
        } else if (activity.equals("fname")) {
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            // message=s;
        } else if (activity.equals("lname")) {
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            // message=s;
        }
        /*else if(activity.equals("mob")){
            T.setText(s);
        }*/
        else if (activity.equals("email")) {
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            // message=s;
        } else if (activity.equals("DeleteAccount")) {
            if (s.equals("1")) {
                Toast.makeText(ctx, "Account Deleted!", Toast.LENGTH_LONG).show();
            } else if (s.equals("0")) {
                Toast.makeText(ctx, "Failed to delete account!", Toast.LENGTH_LONG).show();
            }
        }
    }

    String callRequest(String Url) {
        try {
            //Toast.makeText(ctx,Url,Toast.LENGTH_LONG).show();
            URL url = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            DataInputStream in = new DataInputStream(con.getInputStream());

            StringBuffer output = new StringBuffer();
            String str;
            while ((str = in.readLine()) != null) {
                output.append(str);
            }
            in.close();
            return (output.toString());
        } catch (Exception e) {
        }
        return (null);
    }

    String fileUpload(String requestURL, File uploadFile) {
        String charset = "UTF-8";
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFilePart("image", uploadFile);
            List<String> response = multipart.finish();
            System.out.println("SERVER REPLIED:");
            for (String line : response) {
                System.out.println(line);
            }
            return ("Uploaded");
        } catch (IOException ex) {
            return ("Fail");
        }
    }

    void FetchPic(String requrl) {
        try {
            URL imgUrl = new URL(requrl);
            HttpURLConnection con = (HttpURLConnection) imgUrl.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream inputStream = con.getInputStream();

            bm = BitmapFactory.decodeStream(inputStream);
            img.setImageBitmap(bm);
        } catch (IOException e) {
        }

    }

    void getUserInfo(String fname, String lname, String email, String pwd) {
        GrantPermission G = new GrantPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        //final String filename="userinfo";
        // final Boolean loggedin=true;
        SharedPreferences pos = ctx.getSharedPreferences("userinfo", ctx.MODE_PRIVATE);

        SharedPreferences.Editor write = pos.edit();
        write.putBoolean("loggedin", true);
        write.putString("firstname", fname); //(key,value)
        write.putString("lastname", lname); //(key,value)
        write.putString("emailid", email); //(key,value)
        write.putString("mobileno", mob);
        write.putString("pwd", pwd);
        write.commit();
    }
}



