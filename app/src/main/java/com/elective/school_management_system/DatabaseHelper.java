package com.elective.school_management_system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_INSTRUCTORS = "instructors";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";

    private static final String KEY_INST_NAME = "name";
    private static final String KEY_INST_DEPT = "department";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_INSTRUCTORS_TABLE = "CREATE TABLE " + TABLE_INSTRUCTORS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_INST_NAME + " TEXT,"
                + KEY_INST_DEPT + " TEXT" + ")";
        db.execSQL(CREATE_INSTRUCTORS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTORS);
        onCreate(db);
    }

    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, username);
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { KEY_ID };
        String selection = KEY_USER_EMAIL + " = ?" + " AND " + KEY_USER_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    // --- NEW FUNCTION: Get Username by Email ---
    public String getUsername(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_NAME},
                KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        return "User";
    }

    public boolean addInstructor(String name, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INST_NAME, name);
        values.put(KEY_INST_DEPT, department);
        long result = db.insert(TABLE_INSTRUCTORS, null, values);
        return result != -1;
    }
}