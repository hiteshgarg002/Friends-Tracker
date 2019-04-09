package com.example.hp_u.trackingapp;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.InflaterOutputStream;

public class MyProfile extends Fragment {
    TextView fname,lname,mob,email;
    ImageView profile,profileimg;
    ImageButton editFname,editLname,editMob,editEmail;
    String f_name,l_name,mob_no,email_id,editField,mob_for_profile;
    Button delete,yes,no;
    Context ctx;
    MyDialog D;

    public MyProfile() {
    }

    public MyProfile(String f_name, String l_name, String mob_no, String email_id,ImageView img) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.mob_no = mob_no;
        this.email_id = email_id;
        this.profileimg=img;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root=inflater.inflate(R.layout.activity_my_profile,container,false);

       profile=(ImageView)root.findViewById(R.id.profile_image);
      profile.setImageDrawable(profileimg.getDrawable());
        //profile.setImageResource(R.drawable.ic_my_location_black_24dp);


        fname=(TextView)root.findViewById(R.id.fname);
        lname=(TextView)root.findViewById(R.id.lname);
        mob=(TextView)root.findViewById(R.id.mob);
        email=(TextView)root.findViewById(R.id.email);
        delete=(Button)root.findViewById(R.id.delete);

        editFname=(ImageButton)root.findViewById(R.id.editFname);
        editLname=(ImageButton)root.findViewById(R.id.editLname);
        editMob=(ImageButton)root.findViewById(R.id.editMob);
        editEmail=(ImageButton)root.findViewById(R.id.editEmail);

        fname.setText(f_name);
        lname.setText(l_name);
        mob.setText(mob_no);
        email.setText(email_id);

        editFname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDiaogFname(root);
            }
        });

        editLname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaogLname(root);
            }
        });

       editMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaogMob(root);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaogEmail(root);
            }
        });

        delete.setOnClickListener(new DeleteClick(ctx));

        return root;
    }

    public void showDiaogFname(View v){
        FragmentManager F=getFragmentManager();
        editField="fname";
        EditDialog E=new EditDialog(editField,fname,lname,mob,email);
        E.show(F,"EditFname");
    }

    public void showDiaogLname(View v){
        FragmentManager F=getFragmentManager();
        editField="lname";
        EditDialog E=new EditDialog(editField,fname,lname,mob,email);
        E.show(F,"EditLname");
    }

    public void showDiaogMob(View v){
        FragmentManager F=getFragmentManager();
        editField="mob";
        EditDialog E=new EditDialog(editField,fname,lname,mob,email);

        E.show(F,"EditMob");
    }

    public void showDiaogEmail(View v){
        FragmentManager F=getFragmentManager();
        editField="email";
        EditDialog E=new EditDialog(editField,fname,lname,mob,email);
        E.show(F,"EditEmail");
    }

    class DeleteClick implements View.OnClickListener{
        Context ctx;

        public DeleteClick(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            onCreateDialog(1);
        }
    }
    class MyDialog extends Dialog {
        public MyDialog(Context context) {
        super(context);
        setContentView(R.layout.delete_dialog);
            yes=(Button)findViewById(R.id.yes);
            no=(Button)findViewById(R.id.no);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String qdlt[]={"http://"+ getActivity().getString(R.string.ip) +":8080/TrackingApp/DeleteAccount?mob="+mob.toString()};
                    CallHttpRequest del=new CallHttpRequest(getActivity(),"DeleteAccount");
                    del.execute(qdlt);
                }
            });
            setCancelable(false);
    }
    }
    public Dialog onCreateDialog(int id) {

        switch(id)
        {
            case 1:
            D=new MyDialog(getActivity());
            D.show();
            break;
        }
        return D;
    }
}
