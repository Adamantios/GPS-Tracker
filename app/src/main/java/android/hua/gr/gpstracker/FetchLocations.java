package android.hua.gr.gpstracker;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class FetchLocations extends AsyncTask<Void, Void, Void> {

    /**
     * The RESTful Service URL.
     */
    private static final String REST_URL = "change me/location";
    private Context context;
    private static boolean succeded = false;
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
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                // Get "data" json object
                JSONArray leaders = json.getJSONArray("data");

                // Add every User's Data into the ArrayList
                for (int i = 0; i < leaders.length(); i++) {
                    JSONObject jsonSingleDataRow = leaders.getJSONObject(i);

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

        } catch (Exception ignored) {
        }
    }

    /**
     * Saves all the users with their locations to the phone's Db.
     */
    private void saveLocations() {
        DataManagement dm = new DataManagement(context);
        dm.deleteAllUsersFromDB();

        if (!dm.saveLocationsToDB(users))
            Toast.makeText(context, R.string.save_error, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        GET();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        // Display proper toast message
        if (!succeded)
            Toast.makeText(context, R.string.get_locations_error, Toast.LENGTH_SHORT).show();
        else
            saveLocations();
    }
}
