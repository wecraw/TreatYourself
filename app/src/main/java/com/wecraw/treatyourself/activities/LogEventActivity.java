package com.wecraw.treatyourself.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.R;

import java.util.ArrayList;
import java.util.List;

public class LogEventActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);
    private Button buttonSpends;
    private Button buttonEarns;

    List<Event> events = new ArrayList<Event>();
    List<String> names = new ArrayList<String>();
    List<Integer> ids = new ArrayList<Integer>();


    ArrayAdapter<String> adapter;

    private boolean earns = true;

    private ListView lv;

    //for custom action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_event_menu_bar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_event);

        lv = (ListView) findViewById(R.id.listViewEvents);
        populateListView();
        buttonEarns = (Button) findViewById(R.id.buttonEarns);
        buttonSpends = (Button) findViewById(R.id.buttonSpends);


    }


    //refreshes activity, mostly important for when an event is edited
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        populateListView();
    }

    //loads events into listview
    private void populateListView() {


        names.clear();
        ids.clear();
        events.clear();

        events = db.getAllEvents();

        int j = 0;
        if (earns) {
            for (int i = 0; i < (events.size()); i++) {
                if (events.get(i).isEarns()) {
                    names.add(j, events.get(i).getName());
                    ids.add(j, events.get(i).getId());
                    j++;
                }
            }
        } else {
            for (int i = 0; i < (events.size()); i++) {
                if (!events.get(i).isEarns()) {
                    names.add(j, events.get(i).getName());
                    ids.add(j, events.get(i).getId());
                    j++;
                }
            }
        }

        lv.setAdapter(new CustomAdapter(this, R.layout.list_item, names));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = names.get(position);
                int eventID = ids.get(position);

                Intent intent = new Intent(LogEventActivity.this, EventDetailActivity.class);
                intent.putExtra("event id", eventID);
                startActivity(intent);

            }


        });


    }

    //custom arrayadapter to add in options buttons for each listview entry
    public class CustomAdapter extends ArrayAdapter<String> {

        private int layout;
        private List<String> mObjects;

        private CustomAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.textViewListItemName);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.imageButtonListItemOptions);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(getContext(), v);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu_edit_delete, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            int posMenu = item.getItemId();

                            if (posMenu == R.id.popupEdit) {
                                Intent intent = new Intent(LogEventActivity.this, NewEventActivity.class);
                                intent.putExtra("event id", ids.get(position));
                                startActivity(intent);
                            } else if (posMenu == R.id.popupDelete) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                int eventID = ids.get(position);
                                                db.deleteEvent(db.getEvent(eventID));
                                                populateListView();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(LogEventActivity.this);
                                builder.setMessage(String.format(getString(R.string.confirm_delete), names.get(position)))
                                        .setTitle(R.string.delete)
                                        .setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

                            }
                            return true;
                        }

                    });

                    popup.show(); //showing popup menu
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }

    }

    //helper class for arrayadapter
    public class ViewHolder {
        TextView title;
        ImageButton button;
    }


    //radio button behavior
    public void earns(View view) {
        buttonEarns = (Button) findViewById(R.id.buttonEarns);
        buttonSpends = (Button) findViewById(R.id.buttonSpends);
        buttonEarns.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonEarns.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonSpends.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonSpends.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        earns = true;
        populateListView();
    }

    public void spends(View view) {

        buttonSpends.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonSpends.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonEarns.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonEarns.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        earns = false;

        populateListView();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // ...

        View menuItemView = findViewById(R.id.action_sign_out); // SAME ID AS MENU ID
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.popup_menu_new_event);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                int posMenu = item.getItemId();

                if (posMenu == R.id.popupNewEvent) {
                    Intent intent = new Intent(LogEventActivity.this, NewEventActivity.class);
                    startActivity(intent);
                } else if (posMenu == R.id.popupAddQuickEvent) {
                    Intent intent = new Intent(LogEventActivity.this, NewEventActivity.class);
                    intent.putExtra("quickEvent", true);
                    startActivity(intent);
                }
                return true;
            }

        });

        popupMenu.show();

        return true;
    }
}