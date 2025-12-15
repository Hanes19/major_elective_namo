package com.elective.school_management_system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    private static final int DATABASE_VERSION = 6; // Updated Version

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

    // --- Reports Table ---
    private static final String TABLE_REPORTS = "reports";
    private static final String KEY_REP_ROOM = "room_name";
    private static final String KEY_REP_DESC = "description";
    private static final String KEY_REP_CATEGORY = "category";
    private static final String KEY_REP_STATUS = "status";
    private static final String KEY_REP_DATE = "date_reported";

    // --- NEW: Schedule Table ---
    private static final String TABLE_SCHEDULE = "schedules";
    private static final String KEY_SCH_USER_ID = "user_id";
    private static final String KEY_SCH_SUBJECT = "subject";
    private static final String KEY_SCH_ROOM = "room_name";
    private static final String KEY_SCH_DAY = "day_of_week";
    private static final String KEY_SCH_START = "start_time";
    private static final String KEY_SCH_END = "end_time";

    // --- NEW: Events Table ---
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVT_TITLE = "title";
    private static final String KEY_EVT_DESC = "description";
    private static final String KEY_EVT_DATE = "date";
    private static final String KEY_EVT_TYPE = "type";

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

        // 5. Reports Table
        String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_REP_ROOM + " TEXT,"
                + KEY_REP_DESC + " TEXT,"
                + KEY_REP_CATEGORY + " TEXT,"
                + KEY_REP_STATUS + " TEXT,"
                + KEY_REP_DATE + " TEXT" + ")";
        db.execSQL(CREATE_REPORTS_TABLE);

        // 6. Schedule Table
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SCH_USER_ID + " INTEGER,"
                + KEY_SCH_SUBJECT + " TEXT,"
                + KEY_SCH_ROOM + " TEXT,"
                + KEY_SCH_DAY + " TEXT,"
                + KEY_SCH_START + " TEXT,"
                + KEY_SCH_END + " TEXT" + ")";
        db.execSQL(CREATE_SCHEDULE_TABLE);

        // 7. Events Table
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EVT_TITLE + " TEXT,"
                + KEY_EVT_DESC + " TEXT,"
                + KEY_EVT_DATE + " TEXT,"
                + KEY_EVT_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);

        addInitialData(db);
    }

    private void addInitialData(SQLiteDatabase db) {
        // Initial Rooms
        String sqlRoom = "INSERT INTO " + TABLE_ROOMS + " (" + KEY_ROOM_NAME + ", " + KEY_ROOM_DESC + ", " + KEY_ROOM_AR_ID + ") VALUES (?, ?, ?)";
        db.execSQL(sqlRoom, new Object[]{"Room 101", "General Education Lecture Room", "room_101"});
        db.execSQL(sqlRoom, new Object[]{"Room 102", "Science Laboratory", "room_102"});
        db.execSQL(sqlRoom, new Object[]{"Room 103", "Computer Laboratory 1", "room_103"});
        db.execSQL(sqlRoom, new Object[]{"Faculty Room", "Main Faculty Office", "faculty_room"});

        // Initial Reports
        String sqlRep = "INSERT INTO " + TABLE_REPORTS + " (" + KEY_REP_ROOM + ", " + KEY_REP_DESC + ", " + KEY_REP_CATEGORY + ", " + KEY_REP_STATUS + ", " + KEY_REP_DATE + ") VALUES (?, ?, ?, ?, ?)";
        db.execSQL(sqlRep, new Object[]{"Room 103", "Projector not displaying colors correctly", "Electrical", "Pending", "Oct 24, 2025"});
        db.execSQL(sqlRep, new Object[]{"Room 101", "Aircon leaking water", "Maintenance", "Resolved", "Oct 20, 2025"});

        // Initial Schedule (Dummy data for User ID 1)
        String sqlSch = "INSERT INTO " + TABLE_SCHEDULE + " (" + KEY_SCH_USER_ID + ", " + KEY_SCH_SUBJECT + ", " + KEY_SCH_ROOM + ", " + KEY_SCH_DAY + ", " + KEY_SCH_START + ", " + KEY_SCH_END + ") VALUES (?, ?, ?, ?, ?, ?)";
        // Note: For testing, ensure these match the current day you test on, or add logic to fetch 'today' dynamically.
        // For now, I will add common days.
        db.execSQL(sqlSch, new Object[]{1, "Mobile Development", "Room 103", "Monday", "08:00", "11:00"});
        db.execSQL(sqlSch, new Object[]{1, "Data Structures", "Room 101", "Monday", "13:00", "15:00"});
        db.execSQL(sqlSch, new Object[]{1, "Networking", "Room 102", "Tuesday", "09:00", "12:00"});
        db.execSQL(sqlSch, new Object[]{1, "Ethics", "Room 101", "Wednesday", "10:00", "12:00"});
        db.execSQL(sqlSch, new Object[]{1, "Capstone Project", "Room 103", "Friday", "14:00", "17:00"});

        // Initial Events
        String sqlEvt = "INSERT INTO " + TABLE_EVENTS + " (" + KEY_EVT_TITLE + ", " + KEY_EVT_DESC + ", " + KEY_EVT_DATE + ", " + KEY_EVT_TYPE + ") VALUES (?, ?, ?, ?)";
        db.execSQL(sqlEvt, new Object[]{"Campus Job Fair", "Join us at the Gymnasium for the annual job fair.", "2025-10-25", "General"});
        db.execSQL(sqlEvt, new Object[]{"Library Maintenance", "The library will be closed for electrical repairs.", "2025-10-26", "Emergency"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // --- SCHEDULE METHODS ---
    public Schedule getNextClass(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date now = new Date();
        String currentDay = dayFormat.format(now);
        String currentTime = timeFormat.format(now);

        // Find class today where start_time > now
        String selection = KEY_SCH_USER_ID + "=? AND " + KEY_SCH_DAY + "=? AND " + KEY_SCH_START + " > ?";
        String[] args = new String[]{String.valueOf(userId), currentDay, currentTime};

        Cursor cursor = db.query(TABLE_SCHEDULE, null, selection, args, null, null, KEY_SCH_START + " ASC", "1");

        if (cursor != null && cursor.moveToFirst()) {
            Schedule s = new Schedule(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SCH_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_SUBJECT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_ROOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_DAY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_START)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_END))
            );
            cursor.close();
            return s;
        }
        return null;
    }

    // --- EVENT METHODS ---
    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, null, null, null, null, KEY_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVT_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVT_TYPE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // ============================================================================================
    // --- EXISTING METHODS (USER, ROOMS, INSTRUCTORS, REPORTS) ---
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
        return db.insert(TABLE_USERS, null, values) != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID},
                KEY_USER_EMAIL + "=? AND " + KEY_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
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

    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
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
    public List<AdminUserListActivity.UserItem> getAllStudents() {
        List<AdminUserListActivity.UserItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Join Users and Profiles
        String query = "SELECT u.id, u.username, u.email FROM " + TABLE_USERS + " u " +
                "INNER JOIN " + TABLE_PROFILES + " p ON u.id = p.user_id";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new AdminUserListActivity.UserItem(
                        cursor.getInt(0),
                        cursor.getString(1), // Username
                        cursor.getString(2), // Email
                        "Student"
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Fetch all users who DO NOT have a profile (Guests)
    public List<AdminUserListActivity.UserItem> getAllGuests() {
        List<AdminUserListActivity.UserItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Left Join to find users without profiles
        String query = "SELECT u.id, u.username, u.email FROM " + TABLE_USERS + " u " +
                "LEFT JOIN " + TABLE_PROFILES + " p ON u.id = p.user_id " +
                "WHERE p.user_id IS NULL";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new AdminUserListActivity.UserItem(
                        cursor.getInt(0),
                        cursor.getString(1), // Username
                        cursor.getString(2), // Email
                        "Guest"
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    // Add this method to DatabaseHelper.java

    public boolean addReport(String roomName, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        values.put(KEY_REP_ROOM, roomName);
        values.put(KEY_REP_DESC, description);
        values.put(KEY_REP_CATEGORY, category);
        values.put(KEY_REP_STATUS, "Pending"); // Default status
        values.put(KEY_REP_DATE, currentDate);

        long result = db.insert(TABLE_REPORTS, null, values);
        return result != -1;
    }
}