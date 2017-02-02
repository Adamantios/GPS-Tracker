package android.hua.gr.gpstracker;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    /**
     * The Db's name
     */
    private static final String DATABASE_NAME  = "gps_tracker";

    /**
     * The Db's Table name
     */
    static final String TABLE_USERS = "users";

    /**
     * The Db's version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The users table id column
     */
    static final String ID = "ID";

    /**
     * The user's id column
     */
    static final String USER_ID = "USER_ID";

    /**
     * The user's longitude column
     */
    static final String LONGITUDE = "LONGITUDE";

    /**
     * The user's latitude column
     */
    static final String LATITUDE = "LATITUDE";

    /**
     * The user's date time column
     */
    static final String DT = "DT";

    /**
     * DBHelper's Constructor
     * @param context the context
     */
    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String USERS_TABLE_CREATE =
                "CREATE TABLE " + TABLE_USERS +
                        "(" + ID + " INTEGER PRIMARY KEY NOT NULL,"
                        + USER_ID + " TEXT NOT NULL,"
                        + LONGITUDE + " REAL NOT NULL,"
                        + LATITUDE + " REAL NOT NULL,"
                        + DT + " TEXT NOT NULL" + ");";

        // Execute the DB's creation String
        db.execSQL(USERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the DB and create it again
        db.execSQL("DROP TABLE IF EXISTS " +  TABLE_USERS);
        onCreate(db);
    }
}
