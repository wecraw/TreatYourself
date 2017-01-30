package com.wecraw.treatyourself.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.R;

import java.io.FileOutputStream;
import java.lang.reflect.Array;

public class NewEventActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    private EditText etName;
    private Button buttonSubmit;
    private Button buttonTimed;
    private Button buttonUntimed;
    private Button buttonEarns;
    private Button buttonSpends;
    private TextView value_explain;
    private Spinner spinnerValue;
    private Event event;
    private boolean update = false;


    private static boolean timed = true;
    private static boolean earns = true;



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
        setContentView(R.layout.activity_new_event);

        etName = (EditText) findViewById(R.id.editName);
        buttonSpends = (Button) findViewById(R.id.buttonSpends);
        buttonEarns = (Button) findViewById(R.id.buttonEarns);
        buttonTimed = (Button) findViewById(R.id.buttonTimed);
        buttonUntimed = (Button) findViewById(R.id.buttonUntimed);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        value_explain = (TextView) findViewById(R.id.value_explain);

        //setting up the spinner
        spinnerValue = (Spinner) findViewById(R.id.spinnerValue);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.points_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerValue.setAdapter(adapter);

        etName.addTextChangedListener(textWatcher);

        //handles extras if this activity is launched from the edit dialogue of LogEventActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            //get event
            int eventID = extras.getInt("event id");
            event = db.getEvent(eventID);

            //sets name edittext, spinner, button positions, submit button text
            etName.setText(event.getName());

            if (event.getValue()== GlobalConstants.VALUE_LOW){
                spinnerValue.setSelection(0);
            } else if (event.getValue()==GlobalConstants.VALUE_MID){
                spinnerValue.setSelection(1);
            } else if (event.getValue()==GlobalConstants.VALUE_HIGH){
                spinnerValue.setSelection(2);
            }

            if (event.isEarns()){
                earns(buttonEarns);
            } else {
                spends(buttonSpends);
            }
            if (event.isTimed()){
                timed(buttonTimed);
            } else {
                untimed(buttonUntimed);
            }

            buttonSubmit.setText(R.string.update);

            //used when submitting an update to existing entry
            update = true;

        }







    }

    //checks to see if any text fields are empty and if any are, the submit button is disabled
    private void checkFieldsForEmptyValues() {

        if (TextUtils.isEmpty(etName.getText())) {
            buttonSubmit.setEnabled(false);
        } else {
            buttonSubmit.setEnabled(true);
        }
    }

    //called when submit button is pressed
    public void createEvent(View view) {

        //parse name and value fields for their values
        String name = etName.getText().toString();
        String stringValue = spinnerValue.getSelectedItem().toString();
        int value = Integer.parseInt(stringValue.replaceAll("[\\D]", ""));

        //if new event, gets system time, otherwise uses the original creation time
        long time;
        int id = 0;
        if (!update) {
            time = System.currentTimeMillis();
        } else {
            time = event.getTimeCreated();
            id = event.getId();
        }





        //creates or updates event
        if (!update) {
            Event newEvent = new Event(name, timed, earns, value, time);
            db.addNewEvent(newEvent);
        } else {
            Event newEvent = new Event(id, name, timed, earns, value, time);
            db.updateEvent(newEvent);
        }

        finish();

    }


    //code for 'radio' button behavior
    public void earns(View view) {

        buttonEarns.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonEarns.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonSpends.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonSpends.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        if (timed){
            value_explain.setText(R.string.timed_earns_value);
        } else {
            value_explain.setText(R.string.untimed_earns_value);
        }

        earns=true;
    }
    public void spends(View view) {
        buttonSpends.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonSpends.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonEarns.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonEarns.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        if (timed){
            value_explain.setText(R.string.timed_spends_value);
        } else {
            value_explain.setText(R.string.untimed_spends_value);
        }

        earns=false;
    }
    public void timed(View view) {
        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonTimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonUntimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));


        if (earns){
            value_explain.setText(R.string.timed_earns_value);
        } else {
            value_explain.setText(R.string.timed_spends_value);
        }



        timed=true;
    }
    public void untimed(View view) {

        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonUntimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonTimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        if (earns){
            value_explain.setText(R.string.untimed_earns_value);
        } else {
            value_explain.setText(R.string.untimed_spends_value);
        }

        timed=false;
    }












}
