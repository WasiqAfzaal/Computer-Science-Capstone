package com.WasiqAfzaal.Mobile2App;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "weight_tracker.db";
    private static final int DB_VERSION = 1;

    // USERS TABLE
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // WEIGHTS TABLE
    public static final String TABLE_WEIGHTS = "weights";
    public static final String COL_WEIGHT_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_WEIGHT = "weight";
    public static final String COL_USER_FK = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsers =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                        COL_PASSWORD + " TEXT NOT NULL" +
                        ");";

        String createWeights =
                "CREATE TABLE " + TABLE_WEIGHTS + " (" +
                        COL_WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_DATE + " TEXT NOT NULL, " +
                        COL_WEIGHT + " REAL NOT NULL, " +
                        COL_USER_FK + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + COL_USER_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")" +
                        ");";

        db.execSQL(createUsers);
        db.execSQL(createWeights);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------- LOGIN METHODS ----------
    public boolean usernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = (c != null && c.moveToFirst());
        if (c != null) c.close();
        return exists;
    }

    public long createUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public long validateLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,
                new String[]{COL_USER_ID, COL_PASSWORD},
                COL_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        long userId = -1;
        if (c != null && c.moveToFirst()) {
            String stored = c.getString(c.getColumnIndexOrThrow(COL_PASSWORD));
            if (stored.equals(password)) {
                userId = c.getLong(c.getColumnIndexOrThrow(COL_USER_ID));
            }
        }
        if (c != null) c.close();
        return userId;
    }

    // ---------- WEIGHT CRUD ----------
    public long addWeight(long userId, String date, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_FK, userId);
        values.put(COL_DATE, date);
        values.put(COL_WEIGHT, weight);
        return db.insert(TABLE_WEIGHTS, null, values);
    }

    public Cursor getAllWeightsForUser(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_WEIGHTS,
                null,
                COL_USER_FK + "=?",
                new String[]{String.valueOf(userId)},
                null, null,
                COL_WEIGHT_ID + " DESC");
    }

    public boolean updateWeight(long weightId, String date, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, date);
        values.put(COL_WEIGHT, weight);
        int rows = db.update(TABLE_WEIGHTS,
                values,
                COL_WEIGHT_ID + "=?",
                new String[]{String.valueOf(weightId)});
        return rows > 0;
    }

    public boolean deleteWeight(long weightId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_WEIGHTS,
                COL_WEIGHT_ID + "=?",
                new String[]{String.valueOf(weightId)});
        return rows > 0;
    }
}