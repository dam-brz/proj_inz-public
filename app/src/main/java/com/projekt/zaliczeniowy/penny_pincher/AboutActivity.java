package com.projekt.zaliczeniowy.penny_pincher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.customType(AboutActivity.this, "right-to-left");
    }
}