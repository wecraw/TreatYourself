package com.wecraw.treatyourself.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.GlobalConstants;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.User;

public class ResetActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
    }

    public void resetLog(View v){
        deleteTable(DatabaseOperations.TABLE_LOGENTRY_DETAIL,"log entries");
    }
    public void resetTodos(View v){
        deleteTable(DatabaseOperations.TABLE_TODO_DETAIL,"todos");
    }
    public void resetEvents(View v){
        deleteTable(DatabaseOperations.TABLE_EVENT_DETAIL,"events");
        Long time = System.currentTimeMillis();

        //dont know why these won't add, maybe method ends too soon?
        /* db.addNewEvent(new Event("Lecture",true,true, GlobalConstants.VALUE_MID,time));
        db.addNewEvent(new Event("Walking",true,true,GlobalConstants.VALUE_LOW,time));
        db.addNewEvent(new Event("Running",true,true,GlobalConstants.VALUE_HIGH,time));
        db.addNewEvent(new Event("Homework",true,true,GlobalConstants.VALUE_MID,time));
        db.addNewEvent(new Event("Movie",false,false,GlobalConstants.VALUE_HIGH,time));
        db.addNewEvent(new Event("Video Games",true,false,GlobalConstants.VALUE_MID,time));*/
    }


    private void deleteTable(String TABLE_NAME, String eventType){
        final String table = TABLE_NAME;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        db.deleteAll(table);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you'd like to reset all "+eventType+"?")
                .setTitle(R.string.delete)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void resetPoints(View v){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        User user = db.getUser(1);
                        user.setPoints(0);
                        db.updateUser(user);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you'd like to reset all points?")
                .setTitle(R.string.delete)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
