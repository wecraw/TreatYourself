package com.wecraw.treatyourself.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editTextName = (EditText) findViewById(R.id.editTextName);
        textViewPoints = (TextView) findViewById(R.id.textViewPoints);

        user = db.getUser(1); //this assumes only one user, most definitely bad practice
        //should prolly replace with some sort of 'active user' variable

        editTextName.setText(user.getName());
        textViewPoints.setText(Long.toString(user.getPoints()));

    }

    public void viewLog(View view) {
        Intent intent = new Intent(this, UserLogActivity.class);
        startActivity(intent);
    }

}

