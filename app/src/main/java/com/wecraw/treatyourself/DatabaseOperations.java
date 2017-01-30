package com.wecraw.treatyourself;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by will_000 on 1/10/2017.
 */

public class DatabaseOperations extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String ID = "id";
    public static final String NAME = "Name";
    public static final String TIMED = "IsTimed";
    public static final String EARNS = "IsEarns";
    public static final String VALUE = "Value";
    public static final String TIME_CREATED = "Time_Created";
    public static final String POINTS = "Points";
    public static final String DURATION = "Duration";

    public static final String DATABASE_NAME = "User_Info";
    public static final String TABLE_TODO_DETAIL = "Todo_Info";
    public static final String TABLE_EVENT_DETAIL = "Event_Info";
    public static final String TABLE_USER_DETAIL = "User_Info";
    public static final String TABLE_LOGENTRY_DETAIL = "Log_Info";

    private static final String[] COLUMNS_TODO = {ID,NAME,VALUE,TIME_CREATED};
    private static final String[] COLUMNS_EVENT = {ID,NAME,TIMED,EARNS,VALUE,TIME_CREATED};
    private static final String[] COLUMNS_USER = {ID,NAME,POINTS,TIME_CREATED};
    private static final String[] COLUMNS_LOGENTRY = {ID,NAME,TIME_CREATED,EARNS,TIMED,DURATION,VALUE};


    public DatabaseOperations(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}


    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_EVENT_DETAIL_TABLE = "CREATE TABLE " + TABLE_EVENT_DETAIL + " ( "
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + TIMED + " INTEGER,"
                + EARNS + " INTEGER,"
                + VALUE + " INTEGER,"
                + TIME_CREATED + " INTEGER )";

        String CREATE_USER_DETAIL_TABLE = "CREATE TABLE " + TABLE_USER_DETAIL + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + POINTS + " INTEGER,"
                + TIME_CREATED + " INTEGER )";

        String CREATE_LOGENTRY_DETAIL_TABLE = "CREATE TABLE " + TABLE_LOGENTRY_DETAIL + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + TIME_CREATED + " INTEGER,"
                + EARNS + " INTEGER,"
                + TIMED + " INTEGER,"
                + DURATION + " INTEGER,"
                + VALUE + " INTEGER )";

        String CREATE_TODO_DETAIL_TABLE = "CREATE TABLE " + TABLE_TODO_DETAIL + " ( "
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + VALUE + " INTEGER,"
                + TIME_CREATED + " INTEGER )";

        db.execSQL(CREATE_EVENT_DETAIL_TABLE);
        db.execSQL(CREATE_USER_DETAIL_TABLE);
        db.execSQL(CREATE_LOGENTRY_DETAIL_TABLE);
        db.execSQL(CREATE_TODO_DETAIL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGENTRY_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_DETAIL);
        // create tables again
        onCreate(db);
    }

//CRUD for users

    public void addNewUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, user.getName());
        values.put(POINTS, user.getPoints());
        values.put(TIME_CREATED, user.getTimeCreated());

        db.insert(TABLE_USER_DETAIL, null, values);
        db.close();
    }

    public User getUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USER_DETAIL, COLUMNS_USER, " id = ?", new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();

        user.setId(cursor.getInt(0));
        user.setName(cursor.getString(1));
        user.setPoints(cursor.getInt(2));
        user.setTimeCreated(cursor.getInt(3));

        Log.d("getUser("+id+")", user.toString());

        return user;
    }

    public int updateUser(User user) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName()); // get title
        values.put(POINTS, user.getPoints()); // get author
        values.put(TIME_CREATED, user.getTimeCreated());

        // 3. updating row
        int i = db.update(TABLE_USER_DETAIL, //table
                values, // column/value
                ID+" = ?", // selections
                new String[] { String.valueOf(user.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

//CRUD for events

    public void addNewEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, event.getName());
        values.put(TIMED, event.isTimed());
        values.put(EARNS, event.isEarns());
        values.put(VALUE, event.getValue());
        values.put(TIME_CREATED, event.getTimeCreated());

        //inserting row
        db.insert(TABLE_EVENT_DETAIL, null, values);
        db.close(); //closes database connection
    }

    public int updateEvent(Event event) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(NAME, event.getName());
        values.put(TIMED, event.isTimed());
        values.put(EARNS, event.isEarns());
        values.put(VALUE, event.getValue());
        values.put(TIME_CREATED, event.getTimeCreated());

        // 3. updating row
        int i = db.update(TABLE_EVENT_DETAIL, //table
                values, // column/value
                ID+" = ?", // selections
                new String[] { String.valueOf(event.getId()) }); //selection args

        // 4. close
        db.close();

        Log.d("updated event " + event.getName(), ""+i);

        return i;

    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_EVENT_DETAIL;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Event event = null;
        if (cursor.moveToFirst()) {
            do {
                event = new Event();
                event.setId(cursor.getInt(0));
                event.setName(cursor.getString(1));
                event.setTimed(cursor.getInt(2)>0);
                event.setEarns(cursor.getInt(3)>0);
                event.setValue(cursor.getInt(4));
                event.setTimeCreated(cursor.getInt(5));

                // Add book to books
                events.add(event);
            } while (cursor.moveToNext());
        }

        Log.d("getAllEvents()", events.toString());

        return events;
    }

    public void deleteEvent(Event event) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_EVENT_DETAIL, //table name
                ID+" = ?",  // selections
                new String[] { String.valueOf(event.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", event.toString());

    }

    public Event getEvent(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_EVENT_DETAIL, COLUMNS_EVENT, " id = ?", new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event();
        event.setId(cursor.getInt(0));
        event.setName(cursor.getString(1));
        event.setTimed(cursor.getInt(2)>0);
        event.setEarns(cursor.getInt(3)>0);
        event.setValue(cursor.getInt(4));
        event.setTimeCreated(cursor.getInt(5));

        Log.d("getEvent("+id+")", event.toString());

        return event;

    }

//CRUD for LogEntries

    public void addNewLogEntry(LogEntry logEntry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, logEntry.getName());
        values.put(TIME_CREATED, logEntry.getTimeCreated());
        values.put(EARNS, logEntry.isEarns());
        values.put(TIMED, logEntry.isTimed());
        values.put(DURATION, logEntry.getDuration());
        values.put(VALUE, logEntry.getValue());
        //inserting row
        db.insert(TABLE_LOGENTRY_DETAIL, null, values);
        db.close(); //closes database connection
        Log.d("addNewLogEntry()", logEntry.toString());
    }

    public LogEntry getLogEntry(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_LOGENTRY_DETAIL, COLUMNS_LOGENTRY, " id = ?", new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        LogEntry logEntry = new LogEntry();
        logEntry.setId(cursor.getInt(0));
        logEntry.setName(cursor.getString(1));
        logEntry.setTimeCreated(cursor.getInt(2));
        logEntry.setEarns(cursor.getInt(3)>0);
        logEntry.setTimed(cursor.getInt(4)>0);
        logEntry.setDuration(cursor.getInt(5));
        logEntry.setValue(cursor.getInt(6));

        Log.d("getLogEntry("+id+")", logEntry.toString());

        return logEntry;

    }

    public List<LogEntry> getAllLogEntries() {

        List<LogEntry> logEntries = new ArrayList<LogEntry>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_LOGENTRY_DETAIL;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Event event = null;
        LogEntry logEntry = null;
        if (cursor.moveToFirst()) {
            do {
                logEntry = new LogEntry();
                logEntry.setId(cursor.getInt(0));
                logEntry.setName(cursor.getString(1));
                logEntry.setTimeCreated(cursor.getInt(2));
                logEntry.setEarns(cursor.getInt(3)>0);
                logEntry.setTimed(cursor.getInt(4)>0);
                logEntry.setDuration(cursor.getInt(5));
                logEntry.setValue(cursor.getInt(6));

                logEntries.add(logEntry);
            } while (cursor.moveToNext());
        }

        Log.d("getAllLogEntries()", logEntries.toString());

        return logEntries;
    }

    public void deleteLogEntry(LogEntry logEntry) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_LOGENTRY_DETAIL, //table name
                ID+" = ?",  // selections
                new String[] { String.valueOf(logEntry.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteLogEntry", logEntry.toString());

    }


//CRUD for Todolist
    public void addNewTodo(Todo todo){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(NAME, todo.getName());
    values.put(VALUE, todo.getValue());
    values.put(TIME_CREATED, todo.getTimeCreated());

    //inserting row
    db.insert(TABLE_TODO_DETAIL, null, values);
    db.close(); //closes database connection
}

    public int updateTodo(Todo todo) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(NAME, todo.getName());
        values.put(VALUE, todo.getValue());
        values.put(TIME_CREATED, todo.getTimeCreated());

        // 3. updating row
        int i = db.update(TABLE_TODO_DETAIL, //table
                values, // column/value
                ID+" = ?", // selections
                new String[] { String.valueOf(todo.getId()) }); //selection args

        // 4. close
        db.close();

        Log.d("updated todo " + todo.getName(), ""+i);

        return i;

    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<Todo>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TODO_DETAIL;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Todo todo = null;
        if (cursor.moveToFirst()) {
            do {
                todo = new Todo();
                todo.setId(cursor.getInt(0));
                todo.setName(cursor.getString(1));
                todo.setValue(cursor.getInt(2));
                todo.setTimeCreated(cursor.getInt(3));

                // Add book to books
                todos.add(todo);
            } while (cursor.moveToNext());
        }

        Log.d("getAllEvents()", todos.toString());

        return todos;
    }

    public void deleteTodo(Todo todo) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TODO_DETAIL, //table name
                ID+" = ?",  // selections
                new String[] { String.valueOf(todo.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", todo.toString());

    }

    public Todo getTodo(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_TODO_DETAIL, COLUMNS_TODO, " id = ?", new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Todo todo = new Todo();
        todo.setId(cursor.getInt(0));
        todo.setName(cursor.getString(1));
        todo.setValue(cursor.getInt(4));
        todo.setTimeCreated(cursor.getInt(5));

        Log.d("getTodo("+id+")", todo.toString());

        return todo;

    }
}