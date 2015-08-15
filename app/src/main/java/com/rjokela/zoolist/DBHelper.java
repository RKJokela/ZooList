package com.rjokela.zoolist;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Randon K. Jokela on 8/14/2015.
 */
public final class DBHelper {
    public static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "animal.db";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "animaldata";

    // column names
    public static final String KEY_ID       = "_id";
    public static final String KEY_NAME     = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_TYPE     = "type";

    // column names
    public static final int COLUMN_ID       = 0;
    public static final int COLUMN_NAME     = 1;
    public static final int COLUMN_LOCATION = 2;
    public static final int COLUMN_TYPE     = 3;

    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;

    private static final String INSERT =
            "INSERT INTO " + TABLE_NAME + "(" +
                    KEY_NAME + "," +
                    KEY_LOCATION + "," +
                    KEY_TYPE + ") values (?,?,?)";

    public DBHelper(Context context) throws Exception {
        this.context = context;
        try {
            OpenHelper openHelper = new OpenHelper(this.context);
            // open a database for reading and writing
            db = openHelper.getWritableDatabase();
            // compile a sqlite insert statement into re-usable statement object
            insertStmt = db.compileStatement(INSERT);
        } catch (Exception e) {
            Log.e(TAG, "DBHelper constructor: could not get database " + e);
            throw(e);
        }
    }

    public long insert(Animal animalInfo) {
        // bind values to the pre-compiled SQL statement
        insertStmt.bindString(COLUMN_NAME, animalInfo.getName());
        insertStmt.bindString(COLUMN_LOCATION, animalInfo.getLocation());
        insertStmt.bindString(COLUMN_TYPE, animalInfo.getType());

        long value = -1;
        try {
            // execute the sqlite stmt
            value = insertStmt.executeInsert();
        } catch (Exception e) {
            Log.e(TAG, "executeInsert problem: " + e);
        }
        Log.d(TAG, "insert: value = " + value);
        return value;
    }

    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }

    // delete a row in the database
    public boolean deleteRecord(long rowId) {
        return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
    }

    public List<Animal> selectAll() {
        List<Animal> list = new ArrayList<Animal>();

        // query takes the following parameters
        // dbName : the table name
        // columnNames: a list of which table columns to return
        // whereClause: filter of selection of data; null selects all data
        // selectionArg: values to fill in the ? if any are in the whereClause
        // group by: Filter specifying how to group rows, null means no grouping
        // having: filter for groups, null means none
        // orderBy: Table columsn used to order the data, null means no order.
        // A Cursor provides read-write access to the result set returned by a database query.
        // A Cursor represents the result of the query and points to one row of the query result.

        Cursor cursor = db.query(TABLE_NAME,
                new String[] { KEY_ID, KEY_NAME, KEY_LOCATION, KEY_TYPE },
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Animal animal = new Animal();
                animal.setName(cursor.getString(COLUMN_NAME));
                animal.setLocation(cursor.getString(COLUMN_LOCATION));
                animal.setType(cursor.getString(COLUMN_TYPE));
                animal.setId(cursor.getLong(COLUMN_ID));
                list.add(animal);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return list;
    }

    // helper class for DB creation/update
    private static class OpenHelper extends SQLiteOpenHelper {
        public static final String TAG = "OpenHelper";

        private static final String CREATE_TABLE =
                "CREATE TABLE" +
                TABLE_NAME +
                "(" + KEY_ID + " integer primary key autoincrement, " +
                KEY_NAME + " TEXT, " +
                KEY_LOCATION + " TEXT, " +
                KEY_TYPE + " TEXT);";

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: Could not create SQL database: " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG,"Upgrading database, this will drop tables and recreate.");
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e ) {
                Log.e(TAG, " onUpgrade: Could not update SQL database: " + e);
            }

            // Technique to add a column rather than recreate the tables.
            // String upgradeQuery_ADD_AREA =
            // "ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + KEY_AREA + " TEXT ";
            // if(oldVersion <2 ){
            // db.execSQL(upgradeQuery_ADD_AREA);
        }
    }
}
