package com.wecraw.treatyourself.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.User;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    private TextView textViewUserName;
    private Button buttonLogEvent;
    private Button buttonUserDetail;
    private Button buttonTodo;
    private Button buttonNewEvent;
    private User user;


    DatabaseOperations db = new DatabaseOperations(this);


    final String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogEvent = (Button) findViewById(R.id.buttonLogEvent);
        buttonUserDetail = (Button) findViewById(R.id.buttonUserDetail);
        buttonTodo = (Button) findViewById(R.id.buttonTodo);
        buttonNewEvent = (Button) findViewById(R.id.buttonCreateEvent);
        textViewUserName = (TextView) findViewById(R.id.textViewHey);

        //font stuff
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/menu_font.ttf");
        buttonNewEvent.setTypeface(myTypeface);
        buttonUserDetail.setTypeface(myTypeface);
        buttonTodo.setTypeface(myTypeface);
        buttonLogEvent.setTypeface(myTypeface);

        //myTypeface = Typeface.createFromAsset(getAssets(), "fonts/hey_user_font.ttf");
        textViewUserName.setTypeface(myTypeface);



        //used to show welcome/signup screen on first launch

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (!welcomeScreenShown){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.apply(); // Very important to save the preference
                            //user editor.commit(); if this gets weird
        }

        textViewUserName.setText("Hey "+ db.getUser(1).getName() + "! ");


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
