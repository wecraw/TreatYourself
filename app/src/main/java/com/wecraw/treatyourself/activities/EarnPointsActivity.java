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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.wecraw.treatyourself.DatabaseOperations;
import com.wecraw.treatyourself.Event;
import com.wecraw.treatyourself.LogEntry;
import com.wecraw.treatyourself.R;
import com.wecraw.treatyourself.Todo;
import com.wecraw.treatyourself.User;

import java.util.ArrayList;
import java.util.List;

public class EarnPointsActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);
    private Button buttonTodoList;
    private Button buttonActivities;
    private Button buttonConfirm;

    List<Event> events = new ArrayList<Event>();
    List<String> names = new ArrayList<String>();
    List<Integer> ids = new ArrayList<Integer>();
    List<Todo> todos = new ArrayList<Todo>();

    Todo todo;
    User user;
    LogEntry logEntry;

    int totalValue = 0;
    int totalChecked = 0;

    ArrayAdapter<String> adapter;

    private boolean showingActivities = true;

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
        setContentView(R.layout.activity_earn_points);
        setTitle("Earn Points");

        lv = (ListView) findViewById(R.id.listViewEvents);
        populateListViewActivities();
        buttonActivities = (Button) findViewById(R.id.buttonActivities);
        buttonTodoList = (Button) findViewById(R.id.buttonTodoList);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
    }

    //refreshes activity, mostly important for when an event is edited
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        if (showingActivities){
            populateListViewActivities();
        } else {
            populateListViewTodoList();
        }
    }

    //loads events into listview
    private void populateListViewActivities() {

        names.clear();
        ids.clear();
        events.clear();

        events = db.getAllEvents();

        int j=0;
        for (int i = 0; i < (events.size()); i++) {
            if (events.get(i).isEarns()) {
                names.add(j, events.get(i).getName());
                ids.add(j, events.get(i).getId());
                j++;
            }
        }

        lv.setAdapter(new CustomAdapterActivities(this, R.layout.list_item_log_event, names));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = names.get(position);
                int eventID = ids.get(position);
                Intent intent;
                if (db.getEvent(eventID).isTimed() && db.getEvent(eventID).isEarns()){
                    intent = new Intent(EarnPointsActivity.this, BoostedChoiceActivity.class);
                } else {
                    intent = new Intent(EarnPointsActivity.this, EventDetailActivity.class);
                }
                intent.putExtra("event id", eventID);
                startActivity(intent);

            }


        });

    }
    private void populateListViewTodoList(){
        names.clear();
        ids.clear();
        todos.clear();

        todos = db.getAllTodos();

        for (int i=0; i<todos.size(); i++){
            names.add(i,todos.get(i).getName());
            ids.add(i,todos.get(i).getId());
        }
        lv.setAdapter(new CustomAdapterTodoList(this, R.layout.list_item_todo_list, names));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.toggle();

            }

        });
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    //custom arrayadapter to add in options buttons for each listview entry
    public class CustomAdapterActivities extends ArrayAdapter<String> {

        private int layout;
        private List<String> mObjects;

        private CustomAdapterActivities(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolderActivities mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolderActivities viewHolder = new ViewHolderActivities();
                viewHolder.title = (TextView) convertView.findViewById(R.id.textViewListItemName);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.imageButtonListItemOptions);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolderActivities) convertView.getTag();
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
                                Intent intent = new Intent(EarnPointsActivity.this, NewEventActivity.class);
                                intent.putExtra("event id", ids.get(position));
                                intent.putExtra("update",true);
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
                                                populateListViewActivities();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(EarnPointsActivity.this);
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
    public class ViewHolderActivities {
        TextView title;
        ImageButton button;
    }

    public class CustomAdapterTodoList extends ArrayAdapter<String> {

        private int layout;
        private List<String> mObjects;

        private CustomAdapterTodoList(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolderTodoList mainViewholder = null;
            final Todo todo = db.getTodo(ids.get(position));
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolderTodoList viewHolder = new ViewHolderTodoList();
                viewHolder.title = (TextView) convertView.findViewById(R.id.textViewListItemName);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.imageButtonListItemOptions);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(viewHolder);
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                       @Override
                       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                           if(isChecked){
                               lv.setItemChecked(position,true);
                               totalValue+=todo.getValue();
                               totalChecked+=1;
                           } else {
                               lv.setItemChecked(position,false);
                               totalValue-=todo.getValue();
                               totalChecked-=1;
                           }
                           if (totalChecked > 0){

                               //following if-else controls plural vs singular
                               if (totalChecked ==1) {
                                   buttonConfirm.setText(String.format(getString(R.string.confirm_todo), totalChecked, "", totalValue));
                               } else {
                                   buttonConfirm.setText(String.format(getString(R.string.confirm_todo), totalChecked, "s", totalValue));
                               }
                               buttonConfirm.setVisibility(View.VISIBLE);
                           } else {
                               buttonConfirm.setVisibility(View.GONE);
                           }
                       }
                   }
                );
            }
            mainViewholder = (ViewHolderTodoList) convertView.getTag();
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
                                Intent intent = new Intent(EarnPointsActivity.this, NewTodoActivity.class);
                                intent.putExtra("todo id", ids.get(position));
                                startActivity(intent);
                            } else if (posMenu == R.id.popupDelete) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                int todoID = ids.get(position);
                                                db.deleteTodo(db.getTodo(todoID));
                                                populateListViewTodoList();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(EarnPointsActivity.this);
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
    public class ViewHolderTodoList {
        TextView title;
        ImageButton button;
        CheckBox checkBox;
    }


    //radio button behavior
    public void activities(View view) {
        buttonActivities.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonActivities.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonTodoList.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonTodoList.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        showingActivities = true;
        populateListViewActivities();
    }

    public void todoList(View view) {

        buttonTodoList.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonClicked));
        buttonTodoList.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextClicked));

        buttonActivities.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonUnclicked));
        buttonActivities.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorButtonTextUnclicked));

        showingActivities = false;

        populateListViewTodoList();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // ...
        if(showingActivities) {
            View menuItemView = findViewById(R.id.action_sign_out); // SAME ID AS MENU ID
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            popupMenu.inflate(R.menu.popup_menu_new_event);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    int posMenu = item.getItemId();

                    if (posMenu == R.id.popupNewEvent) {
                        Intent intent = new Intent(EarnPointsActivity.this, NewEventActivity.class);
                        intent.putExtra("earns",true);
                        startActivity(intent);
                    } else if (posMenu == R.id.popupAddQuickEvent) {
                        Intent intent = new Intent(EarnPointsActivity.this, NewEventActivity.class);
                        intent.putExtra("quickEvent", true);
                        intent.putExtra("earns",true);
                        startActivity(intent);
                    }
                    return true;
                }

            });

            popupMenu.show();

            return true;
        } else {
            Intent intent = new Intent(EarnPointsActivity.this, NewTodoActivity.class);
            startActivity(intent);

            return true;
        }

    }
    public void confirmTodos(View view) {

        user = db.getUser(1);
        user.setPoints(user.getPoints()+totalValue);

        for (int i=0; i<lv.getAdapter().getCount(); i++){
            if (lv.isItemChecked(i))
                todo = todos.get(i);
            logEntry = new LogEntry(todo.getName(),todo.getTimeCreated(), true, false, 0, todo.getValue(),true);
            db.addNewLogEntry(logEntry);
            db.deleteTodo(todo);
        }

        db.updateUser(user);
        finish();

    }
}