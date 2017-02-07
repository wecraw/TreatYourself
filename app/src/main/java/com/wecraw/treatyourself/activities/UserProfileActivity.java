package com.wecraw.treatyourself.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.User;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {


    private User user;
    private EditText editTextName;
    private TextView textViewPoints;


    DatabaseOperations db = new DatabaseOperations(this);

    //for custom action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu_bar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editTextName = (EditText) findViewById(R.id.editTextName);
        textViewPoints = (TextView) findViewById(R.id.textViewPoints);

        user = db.getUser(1); //this assumes only one user, most definitely bad practice
        //should prolly replace with some sort of 'active user' variable

        editTextName.setText(user.getName());
        if (user.getPoints()==1){
            textViewPoints.setText(Long.toString(user.getPoints())+"point");
        } else {
            textViewPoints.setText(Long.toString(user.getPoints())+"points");
        }

    }

    //refreshes activity, mostly important for when a log entry is undone
    @Override
    public void onResume() {// After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        user = db.getUser(1);
        textViewPoints.setText(Long.toString(user.getPoints())+" points");
    }

    //saves user name
    @Override public void onPause() {
        super.onPause();
        user.setName(editTextName.getText().toString());
        db.updateUser(user);
    }

    public void viewLog(View view) {
        Intent intent = new Intent(this, UserLogActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(UserProfileActivity.this, SettingsActivity.class);
        startActivity(intent);

        return true;
    }

}

