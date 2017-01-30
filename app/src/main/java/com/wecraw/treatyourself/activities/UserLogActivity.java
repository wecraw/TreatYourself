package com.wecraw.treatyourself.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.LogEntry;
import com.wecraw.treatyourself.R;

import java.util.ArrayList;
import java.util.List;

public class UserLogActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    List<LogEntry> logEntries = new ArrayList<LogEntry>();
    List<String> names = new ArrayList<String>();
    List<Integer> ids = new ArrayList<Integer>();

    ArrayAdapter<String> adapter;

    private ListView lv;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_menu_bar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);

        lv = (ListView) findViewById(R.id.listViewLog);

        populateListView();

    }

    private void populateListView(){

        names.clear();
        ids.clear();

        logEntries = db.getAllLogEntries();

        LogEntry logEntry;

        //populates in reverse so that the list is sorted by date
        Log.d("size of logEntries is " + logEntries.size(),"test");
        //for (int i=logEntries.size()-1; i>=0; i--){
        for (int i=0; i<logEntries.size(); i++){
            names.add(i, logEntries.get(i).getName());
            ids.add(i,logEntries.get(i).getId());
        }


        lv.setAdapter(new UserLogActivity.CustomAdapter(this, R.layout.list_item, names, this));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = names.get(position);
                int eventID = ids.get(position);
            }



        });

    }
    public class CustomAdapter extends ArrayAdapter<String>{
        private int layout;
        private List<String> mObjects;
        private final Context context;
        private final Activity activity;
       // private final List list;

        //public CustomAdapter(Activity activity, ArrayList<String> list) {
        //    this.activity = activity;
        //    this.list = list;
       // }
        private CustomAdapter(Context context, int resource, List<String> objects, Activity activity) {
            super(context,resource,objects);
            mObjects = objects;
            layout = resource;
            this.context = context;
            this.activity = activity;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            ViewHolder view;
            int logEntryID = ids.get(position);
            LogEntry logEntry = db.getLogEntry(logEntryID);

            if(rowView == null)
            {
                // Get a new instance of the row layout view
                LayoutInflater inflater = activity.getLayoutInflater();
                rowView = inflater.inflate(R.layout.listrow, null);

                // Hold the view objects in an object, that way the don't need to be "re-  finded"
                view = new ViewHolder();
                view.name= (TextView) rowView.findViewById(R.id.textViewName);
                view.duration= (TextView) rowView.findViewById(R.id.textViewDuration);
                view.value= (TextView) rowView.findViewById(R.id.textViewValue);

                rowView.setTag(view);
            } else {
                view = (ViewHolder) rowView.getTag();
            }

            /** Set data to your Views. */

            //LogEntry logEntry = db.getLogEntry(position);

            view.name.setText(logEntry.getName());
            if (logEntry.isTimed()){
                view.duration.setText(Integer.toString(logEntry.getDuration())+" minutes");
            } else {
                view.duration.setText("");
            }
            if (logEntry.isEarns()){
                view.value.setText("+"+logEntry.getValue());
            } else {
                view.value.setText("-"+logEntry.getValue());
            }
            return rowView;
        }
    }
    protected static class ViewHolder{
        protected TextView name;
        protected TextView duration;
        protected TextView value;
    }
}
