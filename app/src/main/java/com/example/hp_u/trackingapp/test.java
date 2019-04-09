package com.example.hp_u.trackingapp;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hp-u on 11/11/2017.
 */

public class test extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    String callRequest(String Url) {
        try {
            //Toast.makeText(ctx,Url,Toast.LENGTH_LONG).show();
            URL url = new URL(Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            DataInputStream in=new DataInputStream(con.getInputStream());

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
}
