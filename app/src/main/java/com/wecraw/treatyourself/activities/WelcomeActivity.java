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
import com.wecraw.treatyourself.R;
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

        finish();

    }





}
