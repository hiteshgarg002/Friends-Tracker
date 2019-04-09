package com.example.hp_u.trackingapp;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hp-u on 7/13/2017.
 */

public class CustomAdapter extends ArrayAdapter<Trackings> {
    Context c;
    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Trackings> objects) {
        super(context, resource, objects);
        c=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li=(LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        convertView=li.inflate(R.layout.track_view,parent,false);

        Trackings T=getItem(position);
        TextView date=(TextView)convertView.findViewById(R.id.txtDate);
        TextView add=(TextView)convertView.findViewById(R.id.txtAdd);
        TextView latlng=(TextView)convertView.findViewById(R.id.txtLatLng);

        date.setText(T.getTracking_date().toString());
        add.setText(T.getAddress().toString());
        latlng.setText(T.getLatitude()+" - "+T.getLongitude());
        return convertView;
    }
}
