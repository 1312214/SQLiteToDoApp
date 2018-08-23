package com.duyhoang.sqlitetodoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.duyhoang.sqlitetodoapp.bean.ToDo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rogerh on 5/12/2018.
 */

public class ToDoDBListDBAdapter {

    private static final String TAG = ToDoDBListDBAdapter.class.getSimpleName();
    private static final String DB_NAME = "todolist.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_TODO = "table_todo";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TODO = "to_do";
    private static final String COLUMN_PLACE = "place";


    // CREATE TABLE table_todo(task_id INTEGER PRIMARY KEY, to_do TEXT NOT NULL);
    private static final String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_TODO + "(" + COLUMN_TASK_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TODO + " TEXT NOT NULL, " + COLUMN_PLACE + " TEXT)";

    private Context context;
    private SQLiteDatabase mSQLiteDatabase;
    private static ToDoDBListDBAdapter  toDoListDBAdapterInstance;




    private ToDoDBListDBAdapter(Context context){
        mSQLiteDatabase = new ToDoListSQLiteHelper(context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static ToDoDBListDBAdapter getToDoListDBAdapterInstance(Context context){
        if(toDoListDBAdapterInstance == null)
            toDoListDBAdapterInstance = new ToDoDBListDBAdapter(context);
        return toDoListDBAdapterInstance;
    }

    public boolean add(String todo, String place){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, todo);
        contentValues.put(COLUMN_PLACE, place);
        return mSQLiteDatabase.insert(TABLE_TODO, null, contentValues ) > 0;
    }

    public boolean remove(String taskId){
        return mSQLiteDatabase.delete(TABLE_TODO, "task_id = ?", new String[]{taskId}) > 0;
    }

    public List<ToDo> getAllToDoList(){
        List<ToDo> toDoList = new ArrayList<ToDo>();

        Cursor cursor = mSQLiteDatabase.query(TABLE_TODO, new String[]{COLUMN_TASK_ID, COLUMN_TODO, COLUMN_PLACE}, null,
                null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                ToDo toDo = new ToDo(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                toDoList.add(toDo);
            }
            cursor.close();
        }

        return toDoList;
    }

    public boolean modify(String taskId, String toDo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, toDo);
        return mSQLiteDatabase.update(TABLE_TODO,contentValues, COLUMN_TASK_ID + " = ?", new String[]{taskId}) > 0;

    }

    //================================================================
    // Use for todoprovider class

    public Cursor getCursorForAllToDoList(){
        return mSQLiteDatabase.query(TABLE_TODO, new String[]{COLUMN_TASK_ID, COLUMN_TODO, COLUMN_PLACE},
                null, null, null, null, null);
    }

    public Cursor getCursorForSpecificPlace(String place){
        return mSQLiteDatabase.query(TABLE_TODO, new String[]{COLUMN_TASK_ID, COLUMN_TODO},
                COLUMN_PLACE + " LIKE '%" + place + "%'", null, null, null, null);
    }

    public Cursor getCursorForCount(){
        return mSQLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_TODO, null);
    }

    public long insert(ContentValues contentValues){
        return mSQLiteDatabase.insert(TABLE_TODO, null, contentValues);
    }

    public int delete(String whereClause, String[] whereArgument){
        return mSQLiteDatabase.delete(TABLE_TODO, whereClause, whereArgument);
    }

    public int update(ContentValues contentValues, String whereClause, String[] whereArgument){
        return mSQLiteDatabase.update(TABLE_TODO, contentValues, whereClause, whereArgument);
    }




    private static class ToDoListSQLiteHelper extends SQLiteOpenHelper{

        public ToDoListSQLiteHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion){
            super(context, dbName, factory, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_TODO);

        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            switch(oldVersion){
                case 1:
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_TODO + " ADD COLUMN " + COLUMN_PLACE + " TEXT");
                    break;
            }
        }
    }


}
