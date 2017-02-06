package com.wecraw.treatyourself.activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.wecraw.treatyourself.R;

public class BoostedChoiceActivity extends AppCompatActivity {

    private Button buttonBoost;
    private Button buttonLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boosted_choice);

        buttonBoost = (Button) findViewById(R.id.buttonBoost);
        buttonLog = (Button) findViewById(R.id.buttonLog);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/menu_font.ttf");
        buttonBoost.setTypeface(myTypeface);
        buttonLog.setTypeface(myTypeface);

    }
}
