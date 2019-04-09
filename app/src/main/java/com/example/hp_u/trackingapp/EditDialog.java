package com.example.hp_u.trackingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ContentFrameLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hp-u on 8/5/2017.
 */

public class EditDialog extends DialogFragment {
    String edit,message;
    Button cancle,save;
    EditText newVal;
    TextView fname,lname,mob,email;
    //Fragment F;

    public EditDialog(String edit, TextView fname, TextView lname, TextView mob, TextView email) {
        this.edit = edit;
        this.fname = fname;
        this.lname = lname;
        this.mob = mob;
        this.email = email;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.edit_dialog,container,false);
        getDialog().setTitle("Enter new "+edit);
        cancle=(Button)root.findViewById(R.id.btn_cncl);
        save=(Button)root.findViewById(R.id.btn_save);
        newVal=(EditText)root.findViewById(R.id.newVal);
        setCancelable(false);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.equals("fname")){
                    CallHttpRequest C=new CallHttpRequest(getActivity(),fname,edit,message);
                    String furl[]={"http://"+EditDialog.this.getString(R.string.ip)+":8080/TrackingApp/UpdateProfile?field="+newVal.getText()+"&mobileno="+mob.getText()+"&edit="+edit};
                    C.execute(furl);
                    fname.setText(newVal.getText());
                }
                else if(edit.equals("lname")){
                    CallHttpRequest C=new CallHttpRequest(getActivity(),lname,edit,message);
                    String furl[]={"http://"+EditDialog.this.getString(R.string.ip)+":8080/TrackingApp/UpdateProfile?field="+newVal.getText()+"&mobileno="+mob.getText()+"&edit="+edit};
                    C.execute(furl);
                    lname.setText(newVal.getText());
                }
                /*else if(edit.equals("mob")){
                    CallHttpRequest C=new CallHttpRequest(getActivity(),mob,edit);
                    String furl[]={"http://"+EditDialog.this.getString(R.string.ip)+":8080/TrackingApp/UpdateProfile?field="+mob.getText()};
                    C.execute(furl);
                    Toast.makeText(getActivity(),"EditMob",Toast.LENGTH_LONG).show();
                }*/
                else if(edit.equals("email")){
                    CallHttpRequest C=new CallHttpRequest(getActivity(),email,edit,message);
                    String furl[]={"http://"+EditDialog.this.getString(R.string.ip)+":8080/TrackingApp/UpdateProfile?field="+newVal.getText()+"&mobileno="+mob.getText()+"&edit="+edit};
                    C.execute(furl);
                    email.setText(newVal.getText());
                }
                dismiss();
            }
        });
        return root;
    }

}
