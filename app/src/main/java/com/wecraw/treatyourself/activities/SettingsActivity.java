package com.wecraw.treatyourself.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wecraw.treatyourself.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }
    public void about(View v){
        Intent intent = new Intent(SettingsActivity.this,AboutActivity.class);
        startActivity(intent);
    }
}
