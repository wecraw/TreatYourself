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
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.Todo;
import com.wecraw.treatyourself.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    private TextView textViewUserName;
    private Button buttonLogEvent;
    private Button buttonUserDetail;
    private Button buttonTodo;
    private User user;

    DatabaseOperations db = new DatabaseOperations(this);

    final String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //used to show welcome/signup screen on first launch
        //instead of using prefs, app will just send to welcome screen if no users exist.
        List<User> temp = db.getAllUsers();
        if (temp.size()==0) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);


            temp.clear();
            temp =  db.getAllUsers();
        }

        buttonLogEvent = (Button) findViewById(R.id.buttonLogEvent);
        buttonUserDetail = (Button) findViewById(R.id.buttonUserDetail);
        buttonTodo = (Button) findViewById(R.id.buttonTodo);
        textViewUserName = (TextView) findViewById(R.id.textViewHey);

        //font stuff
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/menu_font.ttf");
        buttonUserDetail.setTypeface(myTypeface);
        buttonTodo.setTypeface(myTypeface);
        buttonLogEvent.setTypeface(myTypeface);

        myTypeface = Typeface.createFromAsset(getAssets(), "fonts/hey_font.ttf");
        textViewUserName.setTypeface(myTypeface);

        if (temp.size()!=0)
            textViewUserName.setText("Hey "+ db.getUser(1).getName() + "! ");

    }

    //refreshes activity, mostly important for when a new username is created
    @Override
    public void onResume() {
        super.onResume();
        //Refresh your stuff here
        List<User> temp = db.getAllUsers();
        if (temp.size()>0)
            textViewUserName.setText("Hey "+ db.getUser(1).getName() + "! ");
    }


    /* Called when the user clicks the Log Event button */
    public void logEvent(View view) {
        Intent intent = new Intent(this, EarnPointsActivity.class);
        startActivity(intent);
    }
    public void yourProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    public void todo(View view) {
        Intent intent = new Intent(this, SpendPointsActivity.class);
        startActivity(intent);
    }

}
