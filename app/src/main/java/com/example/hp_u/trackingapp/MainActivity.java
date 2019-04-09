package com.example.hp_u.trackingapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hp-u on 7/7/2017.
 */

public class MainActivity extends AppCompatActivity {
    EditText email, mobileno, fname, lname, pass, cpass;
    Button submit, clear, login;
    TextView msg;
    ImageView imageV;
    ImageButton imgGet,camera,gallery;
    File destination;
    Context ctx;
    MyDialog D;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        setContentView(R.layout.registration_layout);
        fname = (EditText) findViewById(R.id.firstName);
        lname = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.emailID);
        mobileno = (EditText) findViewById(R.id.phoneNo);
        pass = (EditText) findViewById(R.id.pass);
        cpass = (EditText) findViewById(R.id.conPass);
        imageV = (ImageView) findViewById(R.id.imgView);
        submit = (Button) findViewById(R.id.conReg);
        msg = (TextView) findViewById(R.id.msg);
        imgGet = (ImageButton) findViewById(R.id.btnImg);
        clear = (Button) findViewById(R.id.clearBtn);
        login = (Button) findViewById(R.id.already);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname.setText("");
                lname.setText("");
                email.setText("");
                mobileno.setText("");
                pass.setText("");
                cpass.setText("");
            }
        });

        imgGet.setOnClickListener(new PictureDialog(ctx));

        submit.setOnClickListener(new Submit(ctx));
    }

    class PictureDialog implements View.OnClickListener{
        Context ctx;

        public PictureDialog(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            onCreateDialog(1);
        }
    }

    private void galleryIntent(){
        GrantPermission g = new GrantPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        GrantPermission m = new GrantPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 2);

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }

    private void cameraIntent() {
        GrantPermission g = new GrantPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        GrantPermission m = new GrantPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 2);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            saveImageCamera(data);
        }
        else if(requestCode==2){
            Uri selectedImageURI = data.getData();
            Picasso.with(MainActivity.this).load(selectedImageURI).noPlaceholder().fit()
                    .into(imageV);
            try {
                saveImageGallery(selectedImageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageGallery(Uri data) throws IOException {
        Bitmap thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File("sdcard/",mobileno.getText()+".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImageCamera(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        destination = new File("sdcard/", mobileno.getText() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageV.setImageBitmap(thumbnail);
    }

    class Submit implements View.OnClickListener {
        Context ctx;

        public Submit(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
            try {
                String url[] = {"http://" + MainActivity.this.getString(R.string.ip) + ":8080/TrackingApp/RegistrationSubmit?firstname=" + fname.getText() + "&lastname=" + lname.getText() + "&mobileno=" + mobileno.getText() + "&emailid=" + email.getText() + "&password=" + pass.getText() + "&picture=" + (mobileno.getText() + ".jpg")};
                GrantPermission G = new GrantPermission(MainActivity.this, Manifest.permission.INTERNET, 1);
                CallHttpRequest req = new CallHttpRequest(MainActivity.this, msg, "Registration");
                req.execute(url);

                String fqs = "http://" + getApplicationContext().getString(R.string.ip) + ":8080/TrackingApp/UploadFile";
                // Toast.makeText(ctx,qs,Toast.LENGTH_LONG).show();

                String furl[] = {fqs};
                destination = new File("sdcard/", mobileno.getText() + ".jpg");
                CallHttpRequest freq = new CallHttpRequest(ctx, destination, "FileUpload");
                freq.execute(furl);
            } catch (Exception e) {
            }
        }
    }

    class MyDialog extends Dialog {
        public MyDialog(Context context) {
            super(context);
            setContentView(R.layout.dialog_imagecapture);
            camera=(ImageButton)findViewById(R.id.camera_capture);
            gallery=(ImageButton)findViewById(R.id.gallery_capture);

            camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        cameraIntent();
                        dismiss();

                    } catch (Exception e) {
                        GrantPermission Cam = new GrantPermission(getApplicationContext(), Manifest.permission.CAMERA, 2);
                    }
                }
            });

            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryIntent();
                    dismiss();
                }
            });
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id)
        {
            case 1:
                D=new MyDialog(ctx);
                D.show();
                break;
        }
        return D;
    }
}

