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
    // Version 5 adds the Reports table
    private static final int DATABASE_VERSION = 5;

    // --- User Table ---
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";

    // --- User Profile Table ---
    private static final String TABLE_PROFILES = "user_profiles";
    private static final String KEY_PROFILE_USER_ID = "user_id";
    private static final String KEY_PROFILE_COURSE = "course";
    private static final String KEY_PROFILE_YEAR = "year_level";
    private static final String KEY_PROFILE_SECTION = "section";
    private static final String KEY_PROFILE_PHONE = "phone";

    // --- Instructor Table ---
    private static final String TABLE_INSTRUCTORS = "instructors";
    private static final String KEY_INST_NAME = "name";
    private static final String KEY_INST_DEPT = "department";

    // --- Room Table ---
    private static final String TABLE_ROOMS = "rooms";
    private static final String KEY_ROOM_NAME = "room_name";
    private static final String KEY_ROOM_DESC = "description";
    private static final String KEY_ROOM_AR_ID = "ar_destination_id";

    // --- NEW: Reports Table ---
    private static final String TABLE_REPORTS = "reports";
    private static final String KEY_REP_ROOM = "room_name";
    private static final String KEY_REP_DESC = "description";
    private static final String KEY_REP_CATEGORY = "category"; // e.g., Electrical, Plumbing
    private static final String KEY_REP_STATUS = "status";     // Pending, Resolved
    private static final String KEY_REP_DATE = "date_reported";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // 2. User Profiles Table
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PROFILE_USER_ID + " INTEGER,"
                + KEY_PROFILE_COURSE + " TEXT,"
                + KEY_PROFILE_YEAR + " TEXT,"
                + KEY_PROFILE_SECTION + " TEXT,"
                + KEY_PROFILE_PHONE + " TEXT,"
                + "FOREIGN KEY(" + KEY_PROFILE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_PROFILES_TABLE);

        // 3. Instructors Table
        String CREATE_INSTRUCTORS_TABLE = "CREATE TABLE " + TABLE_INSTRUCTORS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_INST_NAME + " TEXT,"
                + KEY_INST_DEPT + " TEXT" + ")";
        db.execSQL(CREATE_INSTRUCTORS_TABLE);

        // 4. Rooms Table
        String CREATE_ROOMS_TABLE = "CREATE TABLE " + TABLE_ROOMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ROOM_NAME + " TEXT,"
                + KEY_ROOM_DESC + " TEXT,"
                + KEY_ROOM_AR_ID + " TEXT" + ")";
        db.execSQL(CREATE_ROOMS_TABLE);

        // 5. Reports Table (NEW)
        String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_REP_ROOM + " TEXT,"
                + KEY_REP_DESC + " TEXT,"
                + KEY_REP_CATEGORY + " TEXT,"
                + KEY_REP_STATUS + " TEXT,"
                + KEY_REP_DATE + " TEXT" + ")";
        db.execSQL(CREATE_REPORTS_TABLE);

        addInitialData(db);
    }

    private void addInitialData(SQLiteDatabase db) {
        // Initial Rooms
        String sqlRoom = "INSERT INTO " + TABLE_ROOMS + " (" + KEY_ROOM_NAME + ", " + KEY_ROOM_DESC + ", " + KEY_ROOM_AR_ID + ") VALUES (?, ?, ?)";
        db.execSQL(sqlRoom, new Object[]{"Room 101", "General Education Lecture Room", "room_101"});
        db.execSQL(sqlRoom, new Object[]{"Room 102", "Science Laboratory", "room_102"});
        db.execSQL(sqlRoom, new Object[]{"Room 103", "Computer Laboratory 1", "room_103"});
        db.execSQL(sqlRoom, new Object[]{"Faculty Room", "Main Faculty Office", "faculty_room"});

        // Initial Dummy Reports
        String sqlRep = "INSERT INTO " + TABLE_REPORTS + " (" + KEY_REP_ROOM + ", " + KEY_REP_DESC + ", " + KEY_REP_CATEGORY + ", " + KEY_REP_STATUS + ", " + KEY_REP_DATE + ") VALUES (?, ?, ?, ?, ?)";
        db.execSQL(sqlRep, new Object[]{"Room 103", "Projector not displaying colors correctly", "Electrical", "Pending", "Oct 24, 2025"});
        db.execSQL(sqlRep, new Object[]{"Room 101", "Aircon leaking water", "Maintenance", "Resolved", "Oct 20, 2025"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        // Create tables again
        onCreate(db);
    }

    // ============================================================================================
    // --- USER AUTHENTICATION & PROFILE METHODS ---
    // ============================================================================================

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID},
                KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);

        int id = -1;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public boolean saveUserProfile(int userId, String course, String year, String section, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_USER_ID, userId);
        values.put(KEY_PROFILE_COURSE, course);
        values.put(KEY_PROFILE_YEAR, year);
        values.put(KEY_PROFILE_SECTION, section);
        values.put(KEY_PROFILE_PHONE, phone);

        Cursor cursor = db.query(TABLE_PROFILES, new String[]{KEY_ID},
                KEY_PROFILE_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);

        long result;
        if (cursor != null && cursor.getCount() > 0) {
            result = db.update(TABLE_PROFILES, values, KEY_PROFILE_USER_ID + "=?", new String[]{String.valueOf(userId)});
            cursor.close();
        } else {
            result = db.insert(TABLE_PROFILES, null, values);
        }
        return result != -1;
    }

    public Cursor getUserProfile(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PROFILES, null,
                KEY_PROFILE_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
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

    public boolean updateUser(String currentEmail, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, newName);
        values.put(KEY_USER_EMAIL, newEmail);
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

    // ============================================================================================
    // --- ROOM MANAGEMENT METHODS ---
    // ============================================================================================

    public List<Room> getAllRooms() {
        return getRoomsFromQuery(null, null);
    }

    public List<Room> searchRooms(String query) {
        String selection = KEY_ROOM_NAME + " LIKE ? OR " + KEY_ROOM_DESC + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        return getRoomsFromQuery(selection, selectionArgs);
    }

    private List<Room> getRoomsFromQuery(String selection, String[] selectionArgs) {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, selection, selectionArgs, null, null, KEY_ROOM_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                roomList.add(new Room(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_AR_ID))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    public Room getRoomById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Room r = new Room(
                    id,
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_DESC)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_AR_ID))
            );
            cursor.close();
            return r;
        }
        return null;
    }

    public boolean addRoom(String name, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ROOM_NAME, name);
        values.put(KEY_ROOM_DESC, desc);
        // Simple auto-generation for AR ID
        values.put(KEY_ROOM_AR_ID, "room_" + System.currentTimeMillis());
        return db.insert(TABLE_ROOMS, null, values) != -1;
    }

    public boolean updateRoom(int id, String name, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ROOM_NAME, name);
        values.put(KEY_ROOM_DESC, desc);
        return db.update(TABLE_ROOMS, values, KEY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteRoom(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ROOMS, KEY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ============================================================================================
    // --- INSTRUCTOR MANAGEMENT METHODS ---
    // ============================================================================================

    public List<Instructor> getAllInstructors() {
        List<Instructor> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSTRUCTORS, null, null, null, null, null, KEY_INST_NAME + " ASC");
        if(cursor.moveToFirst()){
            do{
                list.add(new Instructor(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_INST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_INST_DEPT))
                ));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean addInstructor(String name, String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INST_NAME, name);
        values.put(KEY_INST_DEPT, department);
        return db.insert(TABLE_INSTRUCTORS, null, values) != -1;
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

    // ============================================================================================
    // --- REPORT MANAGEMENT METHODS (NEW) ---
    // ============================================================================================

    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Order by Status (Pending first) then Date
        Cursor cursor = db.query(TABLE_REPORTS, null, null, null, null, null, KEY_REP_STATUS + " ASC, " + KEY_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Report(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_ROOM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_DATE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Report getReport(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REPORTS, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Report r = new Report(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_ROOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_DESC)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_REP_DATE))
            );
            cursor.close();
            return r;
        }
        return null;
    }

    public boolean updateReportStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REP_STATUS, newStatus);
        return db.update(TABLE_REPORTS, values, KEY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }
}