package com.example.mobilefinal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "travelManager";

    private static final String TABLE_USER = "user";
    private static final String TABLE_TRAVEL = "travel";
    private static final String TABLE_NOTE = "note";
    private static final String TABLE_LOCATION = "location";
    private static final String TABLE_IMAGES = "images";

    // User Table Columns names
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    // Travel Table Columns names
    private static final String KEY_TRAVEL_ID = "id";
    private static final String KEY_LOCATION_ID = "location_id";
    private static final String KEY_USER_ID_FK = "user_id";
    private static final String KEY_NAME = "name";

    // Note Table Columns names
    private static final String KEY_NOTE_ID = "id";
    private static final String KEY_NOTE_TEXT = "note";
    private static final String KEY_TRAVEL_ID_FK = "travel_id";

    // Location Table Columns names
    private static final String KEY_LOC_ID = "id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";

    // Images Table Columns names
    private static final String KEY_IMG_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_TRAVEL_ID_IMG_FK = "travel_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
                + KEY_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_LATITUDE + " REAL" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);

        String CREATE_TRAVEL_TABLE = "CREATE TABLE " + TABLE_TRAVEL + "("
                + KEY_TRAVEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LOCATION_ID + " INTEGER,"
                + KEY_USER_ID_FK + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + "FOREIGN KEY (" + KEY_LOCATION_ID + ") REFERENCES " + TABLE_LOCATION + "(" + KEY_LOC_ID + "),"
                + "FOREIGN KEY (" + KEY_USER_ID_FK + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_ID + ")" + ")";
        db.execSQL(CREATE_TRAVEL_TABLE);

        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + KEY_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOTE_TEXT + " TEXT,"
                + KEY_TRAVEL_ID_FK + " INTEGER,"
                + "FOREIGN KEY (" + KEY_TRAVEL_ID_FK + ") REFERENCES " + TABLE_TRAVEL + "(" + KEY_TRAVEL_ID + ")" + ")";
        db.execSQL(CREATE_NOTE_TABLE);

        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_IMG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_IMAGE + " BLOB,"
                + KEY_TRAVEL_ID_IMG_FK + " INTEGER,"
                + "FOREIGN KEY (" + KEY_TRAVEL_ID_IMG_FK + ") REFERENCES " + TABLE_TRAVEL + "(" + KEY_TRAVEL_ID + ")" + ")";
        db.execSQL(CREATE_IMAGES_TABLE);

        seedDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        onCreate(db);
    }

    private void seedDatabase(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.execSQL("DELETE FROM " + TABLE_TRAVEL);
        db.execSQL("DELETE FROM " + TABLE_LOCATION);
        db.execSQL("DELETE FROM " + TABLE_NOTE);
        db.execSQL("DELETE FROM " + TABLE_IMAGES);

        // Insert user
        ContentValues userValues = new ContentValues();
        userValues.put(KEY_USERNAME, "username");
        userValues.put(KEY_PASSWORD, "password");
        db.insert(TABLE_USER, null, userValues);

        // Insert locations
        long location1Id = insertLocation(db, 40.7128, -74.0060); // New York
        long location2Id = insertLocation(db, 34.0522, -118.2437); // Los Angeles
        long location3Id = insertLocation(db, 51.5074, -0.1278); // London

        // Insert travels
        long travel1Id = insertTravel(db, location1Id, 1, "New York Trip");
        long travel2Id = insertTravel(db, location2Id, 1, "Los Angeles Trip");
        long travel3Id = insertTravel(db, location3Id, 1, "London Trip");

        // Insert notes for each travel
        insertNote(db, travel1Id, "Visited the Statue of Liberty");
        insertNote(db, travel2Id, "Saw the Hollywood Sign");
        insertNote(db, travel3Id, "Toured the British Museum");

        // Insert images for each travel
        insertImage(db, travel1Id, getDummyImageData());
        insertImage(db, travel2Id, getDummyImageData());
        insertImage(db, travel3Id, getDummyImageData());
    }

    public long insertLocation(SQLiteDatabase db, double longitude, double latitude) {
        ContentValues values = new ContentValues();
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_LATITUDE, latitude);
        return db.insert(TABLE_LOCATION, null, values);
    }

    public long insertTravel(SQLiteDatabase db, long locationId, long userId, String name) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_ID, locationId);
        values.put(KEY_USER_ID_FK, userId);
        values.put(KEY_NAME, name);
        return db.insert(TABLE_TRAVEL, null, values);
    }

    public long insertNote(SQLiteDatabase db, long travelId, String noteText) {
        ContentValues values = new ContentValues();
        values.put(KEY_TRAVEL_ID_FK, travelId);
        values.put(KEY_NOTE_TEXT, noteText);
        return db.insert(TABLE_NOTE, null, values);
    }

    public long insertImage(SQLiteDatabase db, long travelId, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(KEY_TRAVEL_ID_IMG_FK, travelId);
        values.put(KEY_IMAGE, image);
        return db.insert(TABLE_IMAGES, null, values);
    }

    // This method generates dummy image data
    private byte[] getDummyImageData() {
        // For simplicity, we are generating a byte array of a small size. In a real application, this would be the byte data of an actual image.
        return new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01};
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);

        if (isUsernameExists(username)) {
            db.close();
            return false;
        }

        // Inserting Row
        long result = db.insert(TABLE_USER, null, values);
        db.close();

        return result != -1;
    }

    private boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USER_ID}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, null, KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }




    public List<Travel> getTravelsByUsername(String username) {
        List<Travel> travels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT t.id as travel_id, t.name, l.id as loc_id, l.longitude, l.latitude " +
                "FROM " + TABLE_TRAVEL + " t " +
                "JOIN " + TABLE_USER + " u ON t.user_id = u.id " +
                "JOIN " + TABLE_LOCATION + " l ON t.location_id = l.id " +
                "WHERE u.username = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int travelId = cursor.getInt(cursor.getColumnIndex("travel_id"));
                @SuppressLint("Range") String travelName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int locationId = cursor.getInt(cursor.getColumnIndex("loc_id"));
                @SuppressLint("Range") float longitude = cursor.getFloat(cursor.getColumnIndex("longitude"));
                @SuppressLint("Range") float latitude = cursor.getFloat(cursor.getColumnIndex("latitude"));

                Location location = new Location(locationId, longitude, latitude);
                List<Note> notes = getNotesByTravelId(travelId);
                List<Images> images = getImagesByTravelId(travelId);

                Travel travel = new Travel();
                travel.setId(travelId);
                travel.setName(travelName);
                travel.setLocation(location);
                travel.setNotes(notes);
                travel.setPhotos(images);

                travels.add(travel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return travels;
    }

    private List<Note> getNotesByTravelId(long travelId) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[]{KEY_NOTE_ID, KEY_NOTE_TEXT},
                KEY_TRAVEL_ID_FK + "=?", new String[]{String.valueOf(travelId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int noteId = cursor.getInt(cursor.getColumnIndex(KEY_NOTE_ID));
                @SuppressLint("Range") String noteText = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TEXT));

                Note note = new Note();
                note.setId(noteId);
                note.setNote(noteText);

                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return notes;
    }

    private List<Images> getImagesByTravelId(long travelId) {
        List<Images> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGES, new String[]{KEY_IMG_ID, KEY_IMAGE},
                KEY_TRAVEL_ID_IMG_FK + "=?", new String[]{String.valueOf(travelId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex(KEY_IMG_ID));
                @SuppressLint("Range") byte[] imageData = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));

                Images image = new Images();
                image.setId(imageId);
                image.setImage(imageData);
                images.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return images;
    }
}
