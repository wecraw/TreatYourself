package com.wecraw.treatyourself.activities;

import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.Todo;

public class NewTodoActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonSubmit;
    private Button buttonEasy;
    private Button buttonMedium;
    private Button buttonHard;
    int value=GlobalConstants.VALUE_LOW;
    Todo todo;
    boolean update = false;

    DatabaseOperations db = new DatabaseOperations(this);

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3){}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonEasy = (Button) findViewById(R.id.buttonEasy);
        buttonMedium = (Button) findViewById(R.id.buttonMedium);
        buttonHard = (Button) findViewById(R.id.buttonHard);

        buttonEasy.setText(String.format(getString(R.string.radio_points),GlobalConstants.VALUE_LOW));
        buttonMedium.setText(String.format(getString(R.string.radio_points),GlobalConstants.VALUE_MID));
        buttonHard.setText(String.format(getString(R.string.radio_points),GlobalConstants.VALUE_HIGH));

        //handles editing
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            todo = db.getTodo(extras.getInt("todo id"));
            editTextName.setText(todo.getName());
            if (todo.getValue() == GlobalConstants.VALUE_LOW) {
                easy(buttonEasy);
            } else if (todo.getValue() == GlobalConstants.VALUE_MID) {
                medium(buttonMedium);
            } else if (todo.getValue() == GlobalConstants.VALUE_HIGH) {
                hard(buttonHard);
            }
            update = true;
            buttonSubmit.setEnabled(true);
        }

        editTextName.addTextChangedListener(textWatcher);
    }

    public void createTodo(View view){
        if (!update)
            todo = new Todo();

        todo.setName(editTextName.getText().toString());
        todo.setValue(value);

        Long time = System.currentTimeMillis();
        todo.setTimeCreated(time);
        if (!update){
            db.addNewTodo(todo);
        } else {
            db.updateTodo(todo);
        }
        finish();
    }

    private void checkFieldsForEmptyValues() {
        if (TextUtils.isEmpty(editTextName.getText())) {
            buttonSubmit.setEnabled(false);
        } else {
            buttonSubmit.setEnabled(true);
        }
    }
    public void easy(View view){
        buttonEasy.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonEasy.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonMedium.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonMedium.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));
        buttonHard.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonHard.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        value = GlobalConstants.VALUE_LOW;
    }
    public void medium(View view){
        buttonMedium.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonMedium.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonEasy.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonEasy.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));
        buttonHard.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonHard.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        value = GlobalConstants.VALUE_MID;
    }
    public void hard(View view){
        buttonHard.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonHard.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonEasy.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonEasy.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));
        buttonMedium.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonMedium.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        value = GlobalConstants.VALUE_HIGH;
    }
}
