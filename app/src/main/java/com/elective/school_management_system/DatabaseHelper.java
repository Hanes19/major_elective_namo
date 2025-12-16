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
    private static final int DATABASE_VERSION = 9; // Incremented version to support Role column

    // --- User Table ---
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_ROLE = "role"; // NEW: Role Column

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

    // --- Schedule Table ---
    private static final String TABLE_SCHEDULE = "schedules";
    private static final String KEY_SCH_USER_ID = "user_id";
    private static final String KEY_SCH_SUBJECT = "subject";
    private static final String KEY_SCH_ROOM = "room_name";
    private static final String KEY_SCH_DAY = "day_of_week";
    private static final String KEY_SCH_START = "start_time";
    private static final String KEY_SCH_END = "end_time";

    // --- Events Table ---
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVT_TITLE = "title";
    private static final String KEY_EVT_DESC = "description";
    private static final String KEY_EVT_DATE = "date";
    private static final String KEY_EVT_TYPE = "type";

    // --- NEW: Enrollment Table (Links Students to Classes) ---
    private static final String TABLE_ENROLLMENTS = "enrollments";
    private static final String KEY_ENROLL_STUDENT_ID = "student_id";
    private static final String KEY_ENROLL_SCHEDULE_ID = "schedule_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Users Table (Updated with Role)
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT,"
                + KEY_USER_ROLE + " TEXT" + ")"; // Added Role
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

        // 8. Enrollments Table
        String CREATE_ENROLLMENTS_TABLE = "CREATE TABLE " + TABLE_ENROLLMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ENROLL_STUDENT_ID + " INTEGER,"
                + KEY_ENROLL_SCHEDULE_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_ENROLL_STUDENT_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_ENROLL_SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULE + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_ENROLLMENTS_TABLE);

        addInitialData(db);
    }

    private void addInitialData(SQLiteDatabase db) {
        // --- 1. Sample Users (Updated with Roles) ---
        // Student (ID 1)
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + KEY_USER_NAME + ", " + KEY_USER_EMAIL + ", " + KEY_USER_PASSWORD + ", " + KEY_USER_ROLE + ") VALUES ('Student Demo', 'student@test.com', 'password123', 'Student')");
        // Teacher (ID 2)
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + KEY_USER_NAME + ", " + KEY_USER_EMAIL + ", " + KEY_USER_PASSWORD + ", " + KEY_USER_ROLE + ") VALUES ('Prof. Severus', 'teacher@test.com', 'password123', 'Teacher')");
        // Admin (ID 3)
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + KEY_USER_NAME + ", " + KEY_USER_EMAIL + ", " + KEY_USER_PASSWORD + ", " + KEY_USER_ROLE + ") VALUES ('Admin User', 'admin@school.com', 'admin123', 'Admin')");

        // --- 2. Sample Rooms ---
        String sqlRoom = "INSERT INTO " + TABLE_ROOMS + " (" + KEY_ROOM_NAME + ", " + KEY_ROOM_DESC + ", " + KEY_ROOM_AR_ID + ") VALUES (?, ?, ?)";
        db.execSQL(sqlRoom, new Object[]{"Room 101", "General Education Lecture Room", "room_101"});
        db.execSQL(sqlRoom, new Object[]{"Room 102", "Science Laboratory", "room_102"});
        db.execSQL(sqlRoom, new Object[]{"Room 103", "Computer Laboratory 1", "room_103"});
        db.execSQL(sqlRoom, new Object[]{"Faculty Room", "Main Faculty Office", "faculty_room"});
        db.execSQL(sqlRoom, new Object[]{"Admin Office", "Administration & HR", "admin_office"});
        db.execSQL(sqlRoom, new Object[]{"Clinic", "School Health Center", "clinic"});

        // --- 3. Sample Schedule (For Teacher ID 2) ---
        int teacherId = 2;
        String sqlSch = "INSERT INTO " + TABLE_SCHEDULE + " (" + KEY_SCH_USER_ID + ", " + KEY_SCH_SUBJECT + ", " + KEY_SCH_ROOM + ", " + KEY_SCH_DAY + ", " + KEY_SCH_START + ", " + KEY_SCH_END + ") VALUES (?, ?, ?, ?, ?, ?)";

        // Monday
        db.execSQL(sqlSch, new Object[]{teacherId, "Adv. Potions", "Room 102", "Monday", "08:00", "10:00"}); // ID 1
        db.execSQL(sqlSch, new Object[]{teacherId, "Defense Against Dark Arts", "Room 101", "Monday", "13:00", "15:00"}); // ID 2

        // Tuesday
        db.execSQL(sqlSch, new Object[]{teacherId, "Alchemy 101", "Room 103", "Tuesday", "09:00", "12:00"}); // ID 3

        // Wednesday
        db.execSQL(sqlSch, new Object[]{teacherId, "Adv. Potions", "Room 102", "Wednesday", "08:00", "10:00"}); // ID 4
        db.execSQL(sqlSch, new Object[]{teacherId, "Ethics in Magic", "Room 101", "Wednesday", "10:00", "12:00"}); // ID 5

        // Friday
        db.execSQL(sqlSch, new Object[]{teacherId, "Staff Meeting", "Faculty Room", "Friday", "16:00", "17:00"}); // ID 6

        // --- 4. Sample Reports ---
        String sqlRep = "INSERT INTO " + TABLE_REPORTS + " (" + KEY_REP_ROOM + ", " + KEY_REP_DESC + ", " + KEY_REP_CATEGORY + ", " + KEY_REP_STATUS + ", " + KEY_REP_DATE + ") VALUES (?, ?, ?, ?, ?)";
        db.execSQL(sqlRep, new Object[]{"Room 103", "Projector not displaying colors correctly", "Electrical", "Pending", "Oct 24, 2025"});
        db.execSQL(sqlRep, new Object[]{"Room 101", "Aircon leaking water", "Maintenance", "Resolved", "Oct 20, 2025"});

        // --- 5. Sample Events ---
        String sqlEvt = "INSERT INTO " + TABLE_EVENTS + " (" + KEY_EVT_TITLE + ", " + KEY_EVT_DESC + ", " + KEY_EVT_DATE + ", " + KEY_EVT_TYPE + ") VALUES (?, ?, ?, ?)";
        db.execSQL(sqlEvt, new Object[]{"Campus Job Fair", "Join us at the Gymnasium for the annual job fair.", "2025-10-25", "General"});
        db.execSQL(sqlEvt, new Object[]{"Library Maintenance", "The library will be closed for electrical repairs.", "2025-10-26", "Emergency"});

        // --- 6. Sample Enrollments (Student ID 1 enrolled in Teacher ID 2's classes) ---
        db.execSQL("INSERT INTO " + TABLE_ENROLLMENTS + " (" + KEY_ENROLL_STUDENT_ID + ", " + KEY_ENROLL_SCHEDULE_ID + ") VALUES (1, 1)");
        db.execSQL("INSERT INTO " + TABLE_ENROLLMENTS + " (" + KEY_ENROLL_STUDENT_ID + ", " + KEY_ENROLL_SCHEDULE_ID + ") VALUES (1, 2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all tables on upgrade to ensure schema changes are applied
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        onCreate(db);
    }

    // ================== DATA ACCESS METHODS ==================

    // --- USER ROLE METHOD (FIXED) ---
    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = "Student"; // Default fallback
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ROLE}, KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            role = cursor.getString(0);
            if (role == null || role.isEmpty()) role = "Student";
        }
        if (cursor != null) cursor.close();
        return role;
    }

    // --- SCHEDULE METHODS ---
    public List<Schedule> getUserSchedule(int userId) {
        List<Schedule> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE, null, KEY_SCH_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_SCH_DAY + " ASC, " + KEY_SCH_START + " ASC");

        if (cursor.moveToFirst()) {
            do {
                list.add(new Schedule(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SCH_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_SUBJECT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_ROOM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_DAY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_START)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_END))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Schedule getNextClass(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date now = new Date();
        String currentDay = dayFormat.format(now);
        String currentTime = timeFormat.format(now);

        String selection = KEY_SCH_USER_ID + "=? AND " + KEY_SCH_DAY + "=? AND " + KEY_SCH_START + " > ?";
        String[] args = new String[]{String.valueOf(userId), currentDay, currentTime};
        Cursor cursor = db.query(TABLE_SCHEDULE, null, selection, args, null, null, KEY_SCH_START + " ASC", "1");

        Schedule s = null;
        if (cursor != null && cursor.moveToFirst()) {
            s = new Schedule(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SCH_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_SUBJECT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_ROOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_DAY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_START)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_END))
            );
        }
        if (cursor != null) cursor.close();
        if (s != null) return s;

        // Fallback
        Cursor cursorAll = db.query(TABLE_SCHEDULE, null, KEY_SCH_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_SCH_DAY + " ASC, " + KEY_SCH_START + " ASC", "1");
        if (cursorAll != null) {
            if (cursorAll.moveToFirst()) {
                s = new Schedule(
                        cursorAll.getInt(cursorAll.getColumnIndexOrThrow(KEY_ID)),
                        cursorAll.getInt(cursorAll.getColumnIndexOrThrow(KEY_SCH_USER_ID)),
                        cursorAll.getString(cursorAll.getColumnIndexOrThrow(KEY_SCH_SUBJECT)),
                        cursorAll.getString(cursorAll.getColumnIndexOrThrow(KEY_SCH_ROOM)),
                        cursorAll.getString(cursorAll.getColumnIndexOrThrow(KEY_SCH_DAY)),
                        cursorAll.getString(cursorAll.getColumnIndexOrThrow(KEY_SCH_START)),
                        cursorAll.getString(cursorAll.getColumnIndexOrThrow(KEY_SCH_END))
                );
            }
            cursorAll.close();
        }
        return s;
    }

    // --- NEW: Get Student's Enrolled Schedule ---
    public List<Schedule> getStudentSchedule(int studentId) {
        List<Schedule> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.* FROM " + TABLE_SCHEDULE + " s " +
                "INNER JOIN " + TABLE_ENROLLMENTS + " e ON s." + KEY_ID + " = e." + KEY_ENROLL_SCHEDULE_ID +
                " WHERE e." + KEY_ENROLL_STUDENT_ID + " = ? " +
                "ORDER BY s." + KEY_SCH_DAY + " ASC, s." + KEY_SCH_START + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        if (cursor.moveToFirst()) {
            do {
                list.add(new Schedule(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SCH_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_SUBJECT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_ROOM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_DAY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_START)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SCH_END))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- Teacher Analytics ---
    public Cursor getEnrollmentCountsForTeacher(int teacherId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s." + KEY_SCH_SUBJECT + ", COUNT(e." + KEY_ID + ") as count " +
                "FROM " + TABLE_SCHEDULE + " s " +
                "LEFT JOIN " + TABLE_ENROLLMENTS + " e ON s." + KEY_ID + " = e." + KEY_ENROLL_SCHEDULE_ID + " " +
                "WHERE s." + KEY_SCH_USER_ID + " = ? " +
                "GROUP BY s." + KEY_SCH_SUBJECT;
        return db.rawQuery(query, new String[]{String.valueOf(teacherId)});
    }

    public Cursor getStudentCountsBySection() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_PROFILE_SECTION + ", COUNT(*) as count " +
                "FROM " + TABLE_PROFILES + " " +
                "WHERE " + KEY_PROFILE_SECTION + " IS NOT NULL AND " + KEY_PROFILE_SECTION + " != '' " +
                "GROUP BY " + KEY_PROFILE_SECTION;
        return db.rawQuery(query, null);
    }

    public boolean enrollStudent(int studentId, int scheduleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENROLL_STUDENT_ID, studentId);
        values.put(KEY_ENROLL_SCHEDULE_ID, scheduleId);
        return db.insert(TABLE_ENROLLMENTS, null, values) != -1;
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

    // --- USER / PROFILE METHODS ---
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID},
                KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        int id = -1;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        if (cursor != null) cursor.close();
        return id;
    }

    public String getUsername(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_NAME},
                KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        String name = "User";
        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        if (cursor != null) cursor.close();
        return name;
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

    // Updated registerUser to include default Role
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, username);
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_PASSWORD, password);
        values.put(KEY_USER_ROLE, "Student"); // Default Role
        return db.insert(TABLE_USERS, null, values) != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_PASSWORD, newPassword);
        return db.update(TABLE_USERS, values, KEY_USER_EMAIL + "=?", new String[]{email}) > 0;
    }

    public Cursor getUserProfile(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PROFILES, null,
                KEY_PROFILE_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
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
        } else {
            result = db.insert(TABLE_PROFILES, null, values);
        }
        if (cursor != null) cursor.close();
        return result != -1;
    }

    public boolean updateUser(String currentEmail, String newName, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, newName);
        values.put(KEY_USER_EMAIL, newEmail);
        return db.update(TABLE_USERS, values, KEY_USER_EMAIL + "=?", new String[]{currentEmail}) > 0;
    }

    // --- ROOM METHODS ---
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
        if (cursor != null) cursor.close();
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

    // --- REPORT METHODS ---
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
        if (cursor != null) cursor.close();
        return null;
    }

    public boolean updateReportStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REP_STATUS, newStatus);
        return db.update(TABLE_REPORTS, values, KEY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean addReport(String roomName, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        values.put(KEY_REP_ROOM, roomName);
        values.put(KEY_REP_DESC, description);
        values.put(KEY_REP_CATEGORY, category);
        values.put(KEY_REP_STATUS, "Pending");
        values.put(KEY_REP_DATE, currentDate);

        long result = db.insert(TABLE_REPORTS, null, values);
        return result != -1;
    }

    // --- DASHBOARD STATISTICS (For AdminDashboardActivity) ---
    public int getTotalUserCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        // Count users with 'Student' role
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE " + KEY_USER_ROLE + "='Student'", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getPendingReportsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REPORTS + " WHERE " + KEY_REP_STATUS + " = 'Pending'", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public String getPendingReportBreakdown() {
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder breakdown = new StringBuilder();
        Cursor cursor = db.rawQuery("SELECT " + KEY_REP_CATEGORY + ", COUNT(*) FROM " + TABLE_REPORTS +
                " WHERE " + KEY_REP_STATUS + " = 'Pending' GROUP BY " + KEY_REP_CATEGORY, null);

        if (cursor.moveToFirst()) {
            do {
                String cat = cursor.getString(0);
                int count = cursor.getInt(1);
                breakdown.append(cat).append(": ").append(count).append("\n");
            } while (cursor.moveToNext());
        } else {
            breakdown.append("No pending reports.");
        }
        cursor.close();
        return breakdown.toString().trim();
    }

    // --- LIST METHODS ---
    public List<AdminUserListActivity.UserItem> getAllStudents() {
        List<AdminUserListActivity.UserItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Assuming AdminUserListActivity.UserItem constructor takes (id, name, email, role/type)
        // Adjust the constructor call below if UserItem differs
        String query = "SELECT " + KEY_ID + ", " + KEY_USER_NAME + ", " + KEY_USER_EMAIL + " FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_ROLE + "='Student'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new AdminUserListActivity.UserItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        "Student"
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<AdminUserListActivity.UserItem> getAllGuests() {
        List<AdminUserListActivity.UserItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Guests: Users who are not Students, Teachers, or Admin
        String query = "SELECT " + KEY_ID + ", " + KEY_USER_NAME + ", " + KEY_USER_EMAIL + " FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_ROLE + " IS NULL OR " + KEY_USER_ROLE + " NOT IN ('Student', 'Teacher', 'Admin')";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new AdminUserListActivity.UserItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        "Guest"
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- INSTRUCTOR METHODS ---
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
}