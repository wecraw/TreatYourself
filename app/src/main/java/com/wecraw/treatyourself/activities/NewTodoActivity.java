package com.wecraw.treatyourself.activities;

import android.provider.Settings;
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
    Spinner spinnerValue;
    Button buttonSubmit;
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
        spinnerValue = (Spinner) findViewById(R.id.spinnerValue);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        //handles editing
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            todo = db.getTodo(extras.getInt("todo id"));
            editTextName.setText(todo.getName());
            if (todo.getValue() == GlobalConstants.VALUE_LOW) {
                spinnerValue.setSelection(0);
            } else if (todo.getValue() == GlobalConstants.VALUE_MID) {
                spinnerValue.setSelection(1);
            } else if (todo.getValue() == GlobalConstants.VALUE_HIGH) {
                spinnerValue.setSelection(2);
            }
            update = true;
        }

        //setting up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.points_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerValue.setAdapter(adapter);

        editTextName.addTextChangedListener(textWatcher);
    }

    public void createTodo(View view){
        if (!update)
            todo = new Todo();

        todo.setName(editTextName.getText().toString());

        String stringValue = spinnerValue.getSelectedItem().toString();
        int value = Integer.parseInt(stringValue.replaceAll("[\\D]", ""));
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
}
