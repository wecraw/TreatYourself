package com.wecraw.treatyourself.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.wecraw.treatyourself.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    private Button buttonLogEvent;
    private Button buttonUserDetail;
    private Button buttonTodo;
    private Button buttonNewEvent;

    final String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogEvent = (Button) findViewById(R.id.buttonLogEvent);
        buttonUserDetail = (Button) findViewById(R.id.buttonUserDetail);
        buttonTodo = (Button) findViewById(R.id.buttonTodo);
        buttonNewEvent = (Button) findViewById(R.id.buttonCreateEvent);

        //font stuff
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/abeezee.italic.ttf");
        buttonNewEvent.setTypeface(myTypeface);
        buttonUserDetail.setTypeface(myTypeface);
        buttonTodo.setTypeface(myTypeface);
        buttonLogEvent.setTypeface(myTypeface);


        //used to show welcome/signup screen on first launch

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (!welcomeScreenShown){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit(); // Very important to save the preference
        }

    }


    /* Called when the user clicks the Log Event button */
    public void logEvent(View view) {
        Intent intent = new Intent(this, LogEventActivity.class);
        startActivity(intent);
    }
    public void newEvent(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }
    public void yourProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
