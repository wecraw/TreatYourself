package com.wecraw.treatyourself.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
        for (int i=logEntries.size()-1; i>=0; i--){
            logEntry = db.getLogEntry(i);

            names.add(i, logEntries.get(i).getName());
            ids.add(i,logEntries.get(i).getId());
        }


        lv.setAdapter(new UserLogActivity.CustomAdapter(this, R.layout.list_item, names));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = names.get(position);
                int eventID = ids.get(position);
            }



        });

    }
   /* public class CustomAdapter extends ArrayAdapter<String> {

        private int layout;
        private List<String> mObjects;
        private final Context context;
        private CustomAdapter(Context context, int resource, List<String> objects) {
            super(context,resource,objects);
            mObjects = objects;
            layout = resource;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = super.getView(position, convertView, parent);
            View rowView = inflater.inflate(R.layout.listrow, parent, false);
            TextView tv = (TextView) rowView.findViewById(android.R.id.text1);
            Event event = db.getEvent(ids.get(position));
            if (event.isEarns()) {
                tv.setTextColor(getColor(R.color.points_green));
            } else {
                tv.setTextColor(getColor(R.color.points_red));
            }

            return view;
        }

    }
    //helper class for arrayadapter
    public class ViewHolder {
        TextView title;
        ImageButton button;
    }
    */


}
