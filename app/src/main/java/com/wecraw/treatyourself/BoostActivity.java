package com.wecraw.treatyourself;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wecraw.treatyourself.activities.LogEventActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.ceil;
import static java.lang.Math.round;
import static java.lang.Math.toIntExact;

public class BoostActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    private Event event;
    private Button buttonGo;
    private Button buttonFinish;
    private TextView textViewEventName;
    private TextView textViewPointsPerMinute;
    private TextView textViewTimer;
    private TextView textViewTotalPoints;
    private ImageView imageViewPause;
    private ProgressBar progressBar;
    private double valuePerMin;
    private int totalPoints;
    private Timer timer;
    private int totalTime;
    private boolean isTimerRunning=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);

        getSupportActionBar().hide(); //hides title bar

        buttonGo = (Button) findViewById(R.id.buttonGo);
        buttonFinish = (Button) findViewById(R.id.buttonFinish);
        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        textViewTotalPoints = (TextView) findViewById(R.id.textViewTotalPoints);
        progressBar = (ProgressBar) findViewById(R.id.fullscreen_content);
        imageViewPause = (ImageView)findViewById(R.id.imageViewPause) ;

        textViewTimer.setVisibility(View.GONE);
        textViewTotalPoints.setVisibility(View.GONE);
        buttonFinish.setVisibility(View.INVISIBLE);

        timer = new Timer();
        Bundle extras = getIntent().getExtras();
        int eventID = extras.getInt("event id");
        event = db.getEvent(eventID);
        int eventPoints = event.getValue()*2; //doubles for boosted productivity

        valuePerMin = eventPoints/ GlobalConstants.MINUTES_FOR_BASE_POINTS;

        textViewEventName = (TextView) findViewById(R.id.textViewEventName);
        textViewEventName.setText(event.getName());

        textViewPointsPerMinute = (TextView) findViewById(R.id.textViewPointsPerMinute);
        textViewPointsPerMinute.setText(String.format(getString(R.string.points_per_minute),valuePerMin));
    }

    @Override
    protected void onPause() {
        super.onPause();

        buttonGo.setVisibility(View.VISIBLE);
        buttonFinish.setVisibility(View.VISIBLE);
        imageViewPause.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        isTimerRunning=false;
        timer.cancel();
    }

    public void beginActivity(View view){
        buttonGo.setVisibility(View.GONE);
        buttonFinish.setVisibility(View.INVISIBLE);
        imageViewPause.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textViewTimer.setVisibility(View.VISIBLE);
        textViewTotalPoints.setVisibility(View.VISIBLE);

        isTimerRunning = true;
        timer = new Timer("alertTimer",true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                totalTime++; //increase every sec
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(tz);
            String time = df.format(new Date(totalTime*1000));
            textViewTimer.setText(time);
            totalPoints=(int) ceil(valuePerMin*totalTime/60.0);
            if (totalPoints==1) {
                textViewTotalPoints.setText(String.format(getString(R.string.boost_total_points),totalPoints,""));
            } else {
                textViewTotalPoints.setText(String.format(getString(R.string.boost_total_points),totalPoints,"s"));
            }

        }
    };
    public void pauseActivity(View view){
        buttonFinish.setVisibility(View.VISIBLE);
        buttonGo.setVisibility(View.VISIBLE);
        imageViewPause.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        isTimerRunning=false;
        timer.cancel();
    }




    public void finishEvent(View view){
        Long time = System.currentTimeMillis();
        int totalTimeMin = (int) ceil(totalTime/60.0);

        LogEntry logEntry = new LogEntry(event.getName(),time,event.isEarns(),true, totalTimeMin, totalPoints);
        db.addNewLogEntry(logEntry);
        User user = db.getUser(1);
        user.setPoints(user.getPoints()+totalPoints);

        db.updateUser(user);
        finish();
    }
}
