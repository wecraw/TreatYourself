package com.wecraw.treatyourself.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.Todo;
import com.wecraw.treatyourself.User;

public class WelcomeActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    private EditText editTextName;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        editTextName = (EditText) findViewById(R.id.editTextName);

        editTextName.addTextChangedListener(textWatcher);
    }

    private void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.buttonSubmit);

        if (TextUtils.isEmpty(editTextName.getText())) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
        }
    }

    public void createUser(View view) {
        String name = editTextName.getText().toString();
        long time = System.currentTimeMillis();

        User newUser = new User(name, 0, time);

        db.addNewUser(newUser);

        db.addNewEvent(new Event("Lecture",true,true, GlobalConstants.VALUE_MID,time));
        db.addNewEvent(new Event("Walking",true,true,GlobalConstants.VALUE_LOW,time));
        db.addNewEvent(new Event("Running",true,true,GlobalConstants.VALUE_HIGH,time));
        db.addNewEvent(new Event("Homework",true,true,GlobalConstants.VALUE_MID,time));
        db.addNewEvent(new Event("Watch a Movie",false,false,GlobalConstants.VALUE_HIGH,time));
        db.addNewEvent(new Event("Play Video Games",true,false,GlobalConstants.VALUE_MID,time));
        db.addNewTodo(new Todo("Download Treat Yourself",GlobalConstants.VALUE_LOW,time));

        finish();

    }





}
