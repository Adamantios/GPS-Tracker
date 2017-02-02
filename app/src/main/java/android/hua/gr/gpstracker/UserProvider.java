package android.hua.gr.gpstracker;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class UserProvider extends ContentProvider {

    /**
     * The content provider's name
     */
    private static final String PROVIDER_NAME =
            "android.hua.gr.gpstracker.contentProvider.UserProvider";

    /**
     * The URL of the Db's "users" table
     */
    private static final String URL = "content://" + PROVIDER_NAME + "/" + DBHelper.TABLE_USERS;

    /**
     * The content URI of the Db's table "users"
     */
    public static final Uri CONTENT_URI = Uri.parse(URL);

    /**
     * The mime type of a directory of users.
     */
    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.android.hua.gr.gpstracker";

    /**
     * The mime type of a single user.
     */

    public static final String CONTENT_SINGLE_USER_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.android.hua.gr.gpstracker";

    /**
     * The users projection map
     */
    @SuppressWarnings("unused")
    private static HashMap<String, String> USERS_PROJECTION_MAP;

    private static final int USERS = 1;
    private static final int USER_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, DBHelper.TABLE_USERS, USERS);
        uriMatcher.addURI(PROVIDER_NAME, DBHelper.TABLE_USERS + "/#", USER_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();

        return db != null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Add a new student record
        long rowID = db.insert(DBHelper.TABLE_USERS, "", values);


        // If record is added successfully
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int insertCount = 0;

        db.beginTransaction();

        for (ContentValues value : values) {
            long id = db.insert(DBHelper.TABLE_USERS, null, value);

            if (id > 0) {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(_uri, null);
            }

            insertCount++;
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return insertCount;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DBHelper.TABLE_USERS);

        switch (uriMatcher.match(uri)) {
            case USERS:
                qb.setProjectionMap(USERS_PROJECTION_MAP);
                break;

            case USER_ID:
                qb.appendWhere(DBHelper.ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        //noinspection StringEquality
        if (sortOrder == null || sortOrder == "") {

            // By default sort on user id
            sortOrder = DBHelper.USER_ID;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            case USERS:
                count = db.delete(DBHelper.TABLE_USERS, selection, selectionArgs);
                break;

            case USER_ID:
                count = db.delete(DBHelper.TABLE_USERS,
                        DBHelper.ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case USERS:
                count = db.update(DBHelper.TABLE_USERS, values, selection, selectionArgs);
                break;

            case USER_ID:
                count = db.update(DBHelper.TABLE_USERS, values,
                        DBHelper.ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND(" + selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all user records
             */
            case USERS:
                return CONTENT_TYPE;

            /**
             * Get a particular user
             */
            case USER_ID:
                return CONTENT_SINGLE_USER_TYPE;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
