package com.duyhoang.sqlitetodoapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.duyhoang.sqlitetodoapp.db.ToDoDBListDBAdapter;

/**
 * Created by rogerh on 5/15/2018.
 */

public class ToDoProvider extends ContentProvider {

    public static final String AUTHORITY = "com.duyhoang.sqlitetodoapp";

    public static final String PATH_TODO_LIST = "TODO_LIST";
    public static final String PATH_TODO_PLACE = "TODO_FROM_PLACE";
    public static final String PATH_TODO_COUNT = "TODO_COUNT";


    public static final Uri CONTENT_URI_1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_LIST) ;
    public static final Uri CONTENT_URI_2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_PLACE);
    public static final Uri CONTENT_URI_3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_COUNT);

    public static final int TODO_LIST_CODE = 1;
    public static final int TODO_PLACE_CODE = 2;
    public static final int TODO_COUNT_CODE = 3;


    public static final String MINE_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.com.duyhoang.todos.list";
    public static final String MINE_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.com.duyhoang.todos.place";
    public static final String MINE_TYPE_3 = ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd.com.duyhoang.todo.count";


    private static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PATH_TODO_LIST, TODO_LIST_CODE);
        MATCHER.addURI(AUTHORITY, PATH_TODO_PLACE, TODO_PLACE_CODE);
        MATCHER.addURI(AUTHORITY, PATH_TODO_COUNT, TODO_COUNT_CODE);
    }

    private ToDoDBListDBAdapter mToDoDBListDBAdapter;


    @Override
    public boolean onCreate() {
        mToDoDBListDBAdapter = ToDoDBListDBAdapter.getToDoListDBAdapterInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] mProject, @Nullable String Selection, @Nullable String[] Argurment, @Nullable String s1) {

        Cursor cursor = null;
        switch (MATCHER.match(uri)){
            case TODO_LIST_CODE: cursor = mToDoDBListDBAdapter.getCursorForAllToDoList(); break;
            case TODO_PLACE_CODE: cursor = mToDoDBListDBAdapter.getCursorForSpecificPlace(Argurment[0]); break;
            case TODO_COUNT_CODE: cursor = mToDoDBListDBAdapter.getCursorForCount(); break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) throws UnsupportedOperationException{
        Uri rs = null;
        switch (MATCHER.match(uri)){
            case TODO_LIST_CODE: rs = insertToDoList(uri, contentValues); break;
            default: throw new UnsupportedOperationException("This insert operation is not supported");
        }
        return rs;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) throws UnsupportedOperationException{
        int rs = -1;

        switch (MATCHER.match(uri)){
            case TODO_LIST_CODE: rs = mToDoDBListDBAdapter.delete(s, strings); break;
            default: throw new UnsupportedOperationException("This Delete operation is not supported");
        }

        return rs;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) throws UnsupportedOperationException {
        int rs = -1;

        switch (MATCHER.match(uri)){
            case TODO_LIST_CODE: rs = mToDoDBListDBAdapter.update(contentValues, s, strings); break;
            default: throw new UnsupportedOperationException("This Update operation is not supported");
        }
        return rs;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (MATCHER.match(uri)){
            case TODO_LIST_CODE: return MINE_TYPE_1;
            case TODO_PLACE_CODE: return MINE_TYPE_2;
            case TODO_COUNT_CODE: return MINE_TYPE_3;
        }
        return null;
    }


    private Uri insertToDoList(Uri uri, ContentValues contentValues){
        long id = mToDoDBListDBAdapter.insert(contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + PATH_TODO_LIST + "/" + id);
    }


}




