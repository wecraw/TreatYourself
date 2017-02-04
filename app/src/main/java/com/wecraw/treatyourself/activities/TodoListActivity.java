package com.wecraw.treatyourself.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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
import android.widget.LinearLayout;
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

import static android.R.id.list;

public class TodoListActivity extends AppCompatActivity {

    DatabaseOperations db = new DatabaseOperations(this);

    List<Todo> todos = new ArrayList<Todo>();
    List<String> names = new ArrayList<String>();
    List<Integer> ids = new ArrayList<Integer>();
    List<Todo> checkedTodos = new ArrayList<Todo>();
    int totalValue = 0;
    int totalChecked = 0;
    User user;
    Todo todo;
    LogEntry logEntry;

    ArrayAdapter<String> adapter;

    private Button buttonConfirm;
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
        setContentView(R.layout.activity_todo_list);

        lv = (ListView) findViewById(R.id.listViewTodos);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setVisibility(View.GONE);
        populateListView();

        setTitle(getString(R.string.todo_list_title));

    }

    //refreshes activity, mostly important for when a todo is added
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        populateListView();
    }


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
            TodoListActivity.ViewHolder mainViewholder = null;
            final Todo todo = db.getTodo(ids.get(position));
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                TodoListActivity.ViewHolder viewHolder = new TodoListActivity.ViewHolder();
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
            mainViewholder = (TodoListActivity.ViewHolder) convertView.getTag();
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
                                Intent intent = new Intent(TodoListActivity.this, NewTodoActivity.class);
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
                                                populateListView();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(TodoListActivity.this);
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
        CheckBox checkBox;
    }

    private void populateListView(){
        names.clear();
        ids.clear();
        todos.clear();

        todos = db.getAllTodos();

        for (int i=0; i<todos.size(); i++){
            names.add(i,todos.get(i).getName());
            ids.add(i,todos.get(i).getId());
        }
        lv.setAdapter(new CustomAdapter(this, R.layout.list_item_todo_list, names));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.toggle();

            }

        });
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(TodoListActivity.this, NewTodoActivity.class);
        startActivity(intent);

        return true;
    }

    public void confirmTodos(View view) {

        user = db.getUser(1);
        user.setPoints(user.getPoints()+totalValue);

        for (int i=0; i<lv.getAdapter().getCount(); i++){
            if (lv.isItemChecked(i))
                todo = todos.get(i);
                logEntry = new LogEntry(todo.getName(),todo.getTimeCreated(), true, false, 0, todo.getValue());
                db.addNewLogEntry(logEntry);
                db.deleteTodo(todo);
        }

        db.updateUser(user);
        finish();

    }

}
