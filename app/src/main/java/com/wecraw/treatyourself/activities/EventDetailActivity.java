package com.wecraw.treatyourself.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.LogEntry;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.User;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.wecraw.treatyourself.R.id.time;
import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.Math.toIntExact;

public class EventDetailActivity extends AppCompatActivity {

    private Event event;
    private User user;
    private LogEntry logEntry;
    private TextView textViewTitle;
    private TextView textViewUserPoints;
    private TextView textViewEventPoints;
    private TextView textViewTotalText;
    private TextView textViewTotalNumber;
    private TextView textViewMinutesPerformed;
    private EditText editTextTime;
    private Button buttonSubmit;
    private long total;
    private double valuePerMin;

    private boolean timed;
    private boolean earns;
    private boolean quickEvent = false;

    //used to check for empty fields on edit text change, also updates total
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();

            if (!TextUtils.isEmpty(editTextTime.getText())) {
                BigDecimal bdTotal = BigDecimal.valueOf(Double.parseDouble(editTextTime.getText().toString()) * valuePerMin);
                bdTotal = bdTotal.setScale(0, RoundingMode.CEILING);
                total = bdTotal.longValue();

                textViewTotalNumber.setText("" + total);
            } else {
                textViewTotalNumber.setText("");

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    DatabaseOperations db = new DatabaseOperations(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            int eventID = extras.getInt("event id");
            event = db.getEvent(eventID);
            if (getIntent().hasExtra("quickEvent")){
                quickEvent = extras.getBoolean("quickEvent");
                db.deleteEvent(event); //deletes the temporary quick event from SQLite db
            }
        }


        user = db.getUser(1); //this assumes only one user, most definitely bad practice
                              //should prolly replace with some sort of 'active user' variable



        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTotalNumber = (TextView) findViewById(R.id.textViewTotalNumber);
        textViewUserPoints = (TextView) findViewById(R.id.textViewUserPoints);
        textViewEventPoints = (TextView) findViewById(R.id.textViewEventPoints);
        textViewTotalText = (TextView) findViewById(R.id.textViewTotalText);
        textViewMinutesPerformed = (TextView) findViewById(R.id.textViewMinutesPerformed);



        editTextTime = (EditText) findViewById(R.id.editTextTime);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);


        int eventPoints = event.getValue();
        valuePerMin = eventPoints/ GlobalConstants.MINUTES_FOR_BASE_POINTS;


        textViewEventPoints.setText(""+eventPoints);
        textViewTitle.setText(event.getName());
        long userPoints = user.getPoints();
        textViewUserPoints.setText(""+userPoints);
        timed = event.isTimed();
        earns = event.isEarns();

        editTextTime.addTextChangedListener(textWatcher);


        //hides appropriate fields if the event is timed
        if (!event.isTimed()){
            editTextTime.setVisibility(View.INVISIBLE);
            textViewTotalText.setVisibility(View.INVISIBLE);
            textViewMinutesPerformed.setVisibility(View.INVISIBLE);
            textViewTotalNumber.setVisibility(View.INVISIBLE);

            total = event.getValue();  //sets total
        }
        if (event.isEarns()){
            textViewTotalText.setText(R.string.total_earned);
        } else {
            textViewTotalText.setText(R.string.total_spent);
        }

    }

    private void checkFieldsForEmptyValues() {

        if (TextUtils.isEmpty(editTextTime.getText())) {
            buttonSubmit.setEnabled(false);
        } else {
            buttonSubmit.setEnabled(true);
        }
    }

    public void submit(View view){

        LogEntry logEntry = new LogEntry();

        if (earns) {
            user.setPoints(user.getPoints() + total);
            db.updateUser(user);
            logEntry.setName(event.getName());
            if (timed) {
                logEntry.setDuration(Integer.parseInt(editTextTime.getText().toString()));
            } else {
                logEntry.setDuration(0);
            }

            logEntry.setValue((int) total);
            logEntry.setTimed(event.isTimed());
            long time = System.currentTimeMillis();
            logEntry.setTimeCreated(time);
            logEntry.setEarns(event.isEarns());

            db.addNewLogEntry(logEntry);

            finish();

        } else {
            if ((user.getPoints() - total) >= 0 ){
                user.setPoints(user.getPoints() - total);
                db.updateUser(user);
                logEntry.setName(event.getName());
                if (timed) {
                    logEntry.setDuration(Integer.parseInt(editTextTime.getText().toString()));
                } else {
                    logEntry.setDuration(0);
                }

                logEntry.setValue((int) total);
                logEntry.setTimed(event.isTimed());
                long time = System.currentTimeMillis();
                logEntry.setTimeCreated(time);
                logEntry.setEarns(event.isEarns());

                db.addNewLogEntry(logEntry);

                finish();
            } else {
                Toast.makeText(getBaseContext(), R.string.insufficient , Toast.LENGTH_SHORT ).show();
            }
        }
    }
}
