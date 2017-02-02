package android.hua.gr.gpstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import java.util.ArrayList;

class DataManagement {

    private Context context;

    /**
     * DataManagement Constructor
     * @param context the context
     */
    DataManagement(Context context) {
        this.context = context;
    }

    /**
     * Deletes all the users from the mobile's DB.
     */
    int deleteAllUsersFromDB() {
        Uri uri = UserProvider.CONTENT_URI;

        return context.getContentResolver().delete(uri, null, null);
    }

    /**
     * Inserts all the users with their locations to the phone's Db.
     * @param users the users to insert
     */
    boolean saveLocationsToDB(ArrayList<User> users) {
        // If there are users, insert them to the mobile's DB
        if (!users.isEmpty()) {
            ContentValues[] valueList = new ContentValues[users.size()];
            int i = 0;

            for (User u : users) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.ID, u.getId());
                values.put(DBHelper.USER_ID, u.getUserId());
                values.put(DBHelper.LONGITUDE, u.getLongitude());
                values.put(DBHelper.LATITUDE, u.getLatitude());
                values.put(DBHelper.DT, u.getDt());
                valueList[i++] = values;
            }

            try {
                context.getContentResolver().bulkInsert(UserProvider.CONTENT_URI, valueList);
            } catch (SQLException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns an ArrayList containing all the users of the mobile's DB
     */
    public ArrayList<User> getAllUsersFromDB() {
        Uri uri = UserProvider.CONTENT_URI;

        String[] projection = new String[]{
                DBHelper.USER_ID,
                DBHelper.LONGITUDE,
                DBHelper.LATITUDE,
                DBHelper.DT
        };

        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        ArrayList<User> users = new ArrayList<>();

        if (c != null) {

            while (c.moveToNext()) {
                User user = new User();
                user.setUserId(c.getString(c.getColumnIndexOrThrow(DBHelper.USER_ID)));
                user.setLongitude(c.getFloat(c.getColumnIndexOrThrow(DBHelper.LONGITUDE)));
                user.setLatitude(c.getFloat(c.getColumnIndexOrThrow(DBHelper.LATITUDE)));
                user.setDt(c.getString(c.getColumnIndexOrThrow(DBHelper.DT)));
                users.add(user);
            }

            c.close();
        }

        return users;
    }
}