package com.lecture.nitika.grub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class StartActivity extends AppCompatActivity {

    private Context mcontext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        mcontext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
            boolean isloggedin = sprefs.getBoolean(Constants.isLoggedIn,false);
            @Override
            public void run() {
                //if logged in directly show the main page
                if(isloggedin){
                    Intent intent = new Intent(mcontext,SearchActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                StartActivity.this.finish();
            }
        }, 5000);




    }
}
