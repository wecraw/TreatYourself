package com.wecraw.treatyourself.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.R;

import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.List;

public class NewEventActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    private EditText etName;
    private Button buttonSubmit;
    private Button buttonTimed;
    private Button buttonUntimed;
    private Button buttonEarns;
    private Button buttonSpends;
    private Button buttonEasy;
    private Button buttonMedium;
    private Button buttonHard;
    private TextView value_explain;
    private TextView quick_event_explain;
    private Event event;
    private boolean update = false;
    private boolean quickEvent = false;
    private int value = GlobalConstants.VALUE_LOW;

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
        buttonEasy = (Button) findViewById(R.id.buttonEasy);
        buttonMedium = (Button) findViewById(R.id.buttonMedium);
        buttonHard = (Button) findViewById(R.id.buttonHard);

        buttonEasy.setText(String.format(getString(R.string.radio_points),GlobalConstants.VALUE_LOW));
        buttonMedium.setText(String.format(getString(R.string.radio_points),GlobalConstants.VALUE_MID));
        buttonHard.setText(String.format(getString(R.string.radio_points),GlobalConstants.VALUE_HIGH));

        value_explain = (TextView) findViewById(R.id.value_explain);
        quick_event_explain = (TextView) findViewById(R.id.textViewQuickEventExplain);

        buttonTimed.setEnabled(false);
        buttonUntimed.setEnabled(false);
        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.radioButtonClickedDisabled));
        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.radioButtonClickedDisabled));
        buttonUntimed.setEnabled(false);

        etName.addTextChangedListener(textWatcher);

        deactivateRadioButtons();

        //handles extras if this activity is launched from the edit dialogue of LogEventActivity
        //or when the event is a quick event
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //if the intent has the extra quickevent, it wont have any others
            if (getIntent().hasExtra("quickEvent")) {
                quickEvent = extras.getBoolean("quickEvent");
                setTitle(getString(R.string.quick_event_title));
                quick_event_explain.setVisibility(View.VISIBLE);

            } else {

                //get event
                int eventID = extras.getInt("event id");
                event = db.getEvent(eventID);

                //sets name edittext, button positions, submit button text
                etName.setText(event.getName());

                if (event.getValue() == GlobalConstants.VALUE_LOW) {
                    easy(buttonEasy);
                } else if (event.getValue() == GlobalConstants.VALUE_MID) {
                    medium(buttonMedium);
                } else if (event.getValue() == GlobalConstants.VALUE_HIGH) {
                    hard(buttonHard);
                }


                if (event.isTimed()) {
                    timed(buttonTimed);
                } else {
                    untimed(buttonUntimed);
                }
                if (event.isEarns()) {
                    earns(buttonEarns);

                } else {
                    spends(buttonSpends);
                }


                buttonSubmit.setText(R.string.update);
                buttonSubmit.setEnabled(true);

                //used when submitting an update to existing entry
                update = true;

                setTitle(getString(R.string.edit_event_title));

            }

        } else { //if there aren't any extras, it's a plain new event, set action bar text accordingly
            setTitle(getString(R.string.new_event_title));
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

            //easiest way to handle passing to event detail is to pass an event ID that is read from db
            //so for a quick event, the event IS saved to the database but is deleted in event detail
            //after the activity is initialized
            if (quickEvent){
                Intent intent = new Intent(NewEventActivity.this, EventDetailActivity.class);
                intent.putExtra("quickEvent", true); //so event detail will know to delete
                List<Event> temp = db.getAllEvents(); //janky way to get the ID of the event that was just created
                intent.putExtra("event id", temp.get(temp.size()-1).getId());
                startActivity(intent);
            } else {
                finish(); //finish needs to be outside of quickevent response in case the back button is pressed
            }
        } else {
            Event newEvent = new Event(id, name, timed, earns, value, time);
            db.updateEvent(newEvent);
            finish();
        }

    }
    //code for 'radio' button behavior
    public void earns(View view) {

        deactivateRadioButtons();

        buttonEarns.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonEarns.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonSpends.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonSpends.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        buttonTimed.setEnabled(false);
        buttonUntimed.setEnabled(false);

        earns=true;
    }
    public void spends(View view) {
        activateRadioButtons();

        buttonSpends.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonSpends.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonEarns.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonEarns.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        buttonTimed.setEnabled(true);
        buttonUntimed.setEnabled(true);

        earns=false;
    }
    public void timed(View view) {

        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonTimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonUntimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        value_explain.setText(R.string.value_explain);

        timed=true;
    }
    public void untimed(View view) {

        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonUntimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonTimed.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        value_explain.setText("");

        timed=false;
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

    public void activateRadioButtons(){

        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.radioButtonClicked));
        buttonTimed.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.radio_button_text_color_clicked));
        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.radioButtonUnclicked));
        buttonUntimed.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.radio_button_text_color_unclicked));

        buttonTimed.setEnabled(true);
        buttonUntimed.setEnabled(true);
    }
    public void deactivateRadioButtons(){

        timed(buttonTimed);

        buttonTimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.radioButtonClickedDisabled));
        buttonTimed.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.radio_button_text_color_clicked_disabled));
        buttonUntimed.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.radioButtonUnclickedDisabled));
        buttonUntimed.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.radio_button_text_color_unclicked_disabled));

        buttonTimed.setEnabled(false);
        buttonUntimed.setEnabled(false);
    }

}
