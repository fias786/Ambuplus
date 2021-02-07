package com.sharnoxz.ambuplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ExpandActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        imageView = findViewById(R.id.ViewImage);
        Intent intent = getIntent();
        imageView.setImageResource(intent.getIntExtra("expand",R.drawable.splashscreen));
    }
}