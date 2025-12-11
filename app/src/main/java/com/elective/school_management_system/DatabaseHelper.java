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
    private static final int DATABASE_VERSION = 2; // Incremented version to trigger onUpgrade

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";

    // Instructor Table
    private static final String TABLE_INSTRUCTORS = "instructors";
    private static final String KEY_INST_NAME = "name";
    private static final String KEY_INST_DEPT = "department";

    // Room Table (NEW)
    private static final String TABLE_ROOMS = "rooms";
    private static final String KEY_ROOM_NAME = "room_name";
    private static final String KEY_ROOM_DESC = "description";
    private static final String KEY_ROOM_AR_ID = "ar_destination_id";

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

        String CREATE_ROOMS_TABLE = "CREATE TABLE " + TABLE_ROOMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ROOM_NAME + " TEXT,"
                + KEY_ROOM_DESC + " TEXT,"
                + KEY_ROOM_AR_ID + " TEXT" + ")";
        db.execSQL(CREATE_ROOMS_TABLE);

        // Pre-populate with dummy data
        addInitialRooms(db);
    }

    private void addInitialRooms(SQLiteDatabase db) {
        // Helper to insert data directly during creation
        String sql = "INSERT INTO " + TABLE_ROOMS + " (" + KEY_ROOM_NAME + ", " + KEY_ROOM_DESC + ", " + KEY_ROOM_AR_ID + ") VALUES (?, ?, ?)";
        db.execSQL(sql, new Object[]{"Room 101", "General Education Lecture Room", "room_101"});
        db.execSQL(sql, new Object[]{"Room 102", "Science Laboratory", "room_102"});
        db.execSQL(sql, new Object[]{"Room 103", "Computer Laboratory 1", "room_103"});
        db.execSQL(sql, new Object[]{"Faculty Room", "Main Faculty Office", "faculty_room"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        onCreate(db);
    }

    // --- USER FUNCTIONS ---
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
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID},
                KEY_USER_EMAIL + "=? AND " + KEY_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

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

    // --- ROOM FUNCTIONS ---
    public List<Room> getAllRooms() {
        return getRoomsFromQuery(null, null);
    }

    public List<Room> searchRooms(String query) {
        return getRoomsFromQuery(KEY_ROOM_NAME + " LIKE ?", new String[]{"%" + query + "%"});
    }

    private List<Room> getRoomsFromQuery(String selection, String[] selectionArgs) {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, selection, selectionArgs, null, null, KEY_ROOM_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_DESC));
                String arId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_AR_ID));
                roomList.add(new Room(id, name, desc, arId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    public Room getRoomById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_NAME));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_DESC));
            String arId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_AR_ID));
            cursor.close();
            return new Room(id, name, desc, arId);
        }
        return null;
    }

    // --- INSTRUCTOR FUNCTIONS ---
    public boolean addInstructor(String name, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INST_NAME, name);
        values.put(KEY_INST_DEPT, department);
        long result = db.insert(TABLE_INSTRUCTORS, null, values);
        return result != -1;
    }

    // --- INSTRUCTOR FUNCTIONS (Add these below addInstructor) ---

    public List<Instructor> getAllInstructors() {
        List<Instructor> instructorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSTRUCTORS, null, null, null, null, null, KEY_INST_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_INST_NAME));
                String dept = cursor.getString(cursor.getColumnIndexOrThrow(KEY_INST_DEPT));
                instructorList.add(new Instructor(id, name, dept));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return instructorList;
    }

    public boolean updateInstructor(int id, String name, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INST_NAME, name);
        values.put(KEY_INST_DEPT, department);
        return db.update(TABLE_INSTRUCTORS, values, KEY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteInstructor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INSTRUCTORS, KEY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

// --- USER PROFILE FUNCTIONS (Add these below getUsername) ---

    public boolean updateUser(String currentEmail, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, newName);
        values.put(KEY_USER_EMAIL, newEmail);
        // return true if update is successful
        return db.update(TABLE_USERS, values, KEY_USER_EMAIL + "=?", new String[]{currentEmail}) > 0;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_PASSWORD, newPassword);
        return db.update(TABLE_USERS, values, KEY_USER_EMAIL + "=?", new String[]{email}) > 0;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


}