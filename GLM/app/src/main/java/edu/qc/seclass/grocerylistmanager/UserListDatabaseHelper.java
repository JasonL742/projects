package edu.qc.seclass.grocerylistmanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserListDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "glm.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user_lists";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GROCERY_LISTS = "grocery_lists";

    public UserListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_GROCERY_LISTS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUserList(UserList userList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, userList.getName());
        values.put(COLUMN_GROCERY_LISTS, convertListToString(userList.getGroceryListNames()));
        long id = db.insert(TABLE_NAME, null, values);
        Log.e("addUserList", "Added: " + values);
        db.close();
        return id;
    }

    public List<UserList> getAllUserLists() {
        List<UserList> userLists = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String groceryListsString = cursor.getString(cursor.getColumnIndex(COLUMN_GROCERY_LISTS));
                List<String> groceryLists = convertStringToList(groceryListsString);
                Log.e("getuserlists", groceryLists.toString() + " " + name);
                UserList userList = new UserList(id, name, groceryLists);
                userLists.add(userList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userLists;
    }

    private String convertListToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list) {
            stringBuilder.append(item).append(",");
        }
        return stringBuilder.toString();
    }

    private List<String> convertStringToList(String string) {
        List<String> list = new ArrayList<>();
        String[] array = string.split(",");
        for (String item : array) {
            list.add(item);
        }
        Log.e("convertStr", list.toString());
        return list;
    }

    public boolean updateUserList(int userListId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GROCERY_LISTS, newName);
        int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(userListId)});
        db.close();
        return rowsUpdated > 0;
    }



    public void deleteUserList(int userListId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(userListId)});
        db.close();
    }





}