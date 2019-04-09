package com.example.hp_u.trackingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private View mLoginFormView;
    Button B1,B2;
    String mssg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        B1 = (Button) findViewById(R.id.email_sign_in_button);
        B1.setOnClickListener(new Btn_Click(this));

        B2 = (Button) findViewById(R.id.button_sign_up);
        B2.setOnClickListener(new Btn_Click(this));
        mLoginFormView = findViewById(R.id.login_form);

    }

    class Btn_Click implements android.view.View.OnClickListener {

        Context ctx;
        Btn_Click(Context ctx) {
            this.ctx=ctx;
        }
        @Override
        public void onClick(View v) {
            if(mEmailView.getText().toString().isEmpty()){
                mEmailView.setError("Field can not be empty!");
            }
            else {
                if (v == B1) {
                    CallHttpRequest C1 = new CallHttpRequest(ctx, "GetUserInfo");
                    String info[] = {"http://" + LoginActivity.this.getString(R.string.ip) + ":8080/TrackingApp/GetUserInfoJSON?user=" + mEmailView.getText() + "&password=" + mPasswordView.getText()};

                    C1.execute(info);

                    CallHttpRequest C = new CallHttpRequest(ctx, "CheckLogin", mssg);
                    String url[] = {"http://" + LoginActivity.this.getString(R.string.ip) + ":8080/TrackingApp/CheckLogin?user=" + mEmailView.getText() + "&password=" + mPasswordView.getText()};

                    C.execute(url);
                    //Snackbar.make(linearLayout,mssg,Snackbar.LENGTH_LONG).show();

                } else if (v == B2) {
                    Intent I = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(I);
                }
            }
        }
    }


}















