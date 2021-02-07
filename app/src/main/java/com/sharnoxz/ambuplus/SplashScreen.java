package com.sharnoxz.ambuplus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sharnoxz.ambuplus.accounts.SharedPrefs;
import com.sharnoxz.ambuplus.accounts.login;


public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean check = Boolean.valueOf(SharedPrefs.readSharedSetting(this,"sharedPrefs","true"));
        if(check){
            startActivity(new Intent(this, login.class));
            finish();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
