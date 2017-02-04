package android.hua.gr.gpstracker;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

class FetchLocations extends AsyncTask<Void, Void, Void> {

    /**
     * The RESTful Service URL.
     */
    private static final String REST_URL = "http://62.217.127.19:8000/location";
    private Context context;
    private static boolean succeded = false;
    private static boolean serverRefusedConnection = false;
    private static ArrayList<User> users = new ArrayList<>();

    FetchLocations(Context context) {
        this.context = context;
    }

    @SuppressWarnings("deprecation")
    private static void GET() {
        try {
            // Create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // Make POST request to the given URL
            HttpGet httpGet = new HttpGet(REST_URL);

            HttpResponse response = httpclient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                // Create a JSON object from the response
                JSONArray json = new JSONArray(EntityUtils.toString(response.getEntity()));

                // Add every User's Data into the ArrayList
                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonSingleDataRow = json.getJSONObject(i);

                    int id = jsonSingleDataRow.getInt("id");
                    String user_id = jsonSingleDataRow.getString("userid");
                    float longitude = (float) jsonSingleDataRow.getDouble("longitude");
                    float latitude = (float) jsonSingleDataRow.getDouble("latitude");
                    String dt = jsonSingleDataRow.getString("latitude");

                    User user = new User();
                    user.setId(id);
                    user.setUserId(user_id);
                    user.setLongitude(longitude);
                    user.setLatitude(latitude);
                    user.setDt(dt);

                    users.add(user);
                }

                succeded = true;
            }

        } catch (org.apache.http.conn.HttpHostConnectException e) {
            serverRefusedConnection = true;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all the users with their locations to the phone's Db.
     */
    private void saveLocations() {
        DataManagement dm = new DataManagement(context);
        dm.deleteAllUsersFromDB();

        if (!dm.saveLocationsToDB(users)) {
            Toast toast = Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(Color.GREEN);
            toast.show();
        }

        for (User user : dm.getAllUsersFromDB())
            Log.d("------------\n\nTHE USERS", user.getUserId() + user.getUserId() + user.getLatitude() + user.getLongitude() + user.getDt() + "\n");
    }

    @Override
    protected Void doInBackground(Void... params) {
        GET();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        // Display proper toast message
        if (serverRefusedConnection) {
            Toast toast = Toast.makeText(context, R.string.server_refused_connection,
                    Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        } else if (!succeded) {
            Toast toast = Toast.makeText(context, R.string.get_locations_error, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        }
        else
            saveLocations();
    }
}
