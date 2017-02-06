package android.hua.gr.gpstracker;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

class RegisterLocation extends AsyncTask<Void, Void, Void> {

    /**
     * The RESTful Service URL.
     */
    private static final String REST_URL = "http://62.217.127.19:8000/location";
    private static Double longitude;
    private static Double latitude;
    private Context context;
    private boolean succeded = false;
    private static boolean serverRefusedConnection = false;

    RegisterLocation(Location lastLocation, Context context) {
        longitude = lastLocation.getLongitude();
        latitude = lastLocation.getLatitude();
        this.context = context;
    }

    @SuppressWarnings("deprecation")
    private static boolean POST() {
        try {
            // Create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // Make POST request to the given URL
            HttpPost httpPost = new HttpPost(REST_URL);

            // Build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("userid", "it21222");
            jsonObject.accumulate("longitude", longitude);
            jsonObject.accumulate("latitude", latitude);
            jsonObject.accumulate("dt", android.text.format.DateFormat.format("yyyy-MM-dd;hh:mm:ss",
                    new java.util.Date()));

            // Convert JSONObject to String
            String json = jsonObject.toString();

            // Set json to StringEntity
            StringEntity stringEntity = new StringEntity(json, HTTP.UTF_8);

            // Set httpPost Entity
            httpPost.setEntity(stringEntity);

            // Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // Receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();

            return inputStream != null;

        } catch (org.apache.http.conn.HttpHostConnectException e) {
            serverRefusedConnection = true;
            return false;
        } catch (IOException | JSONException e) {
            return false;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (POST())
            succeded = true;

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        // Display proper toast message
        if (serverRefusedConnection) {
            Toast toast = Toast.makeText(context, R.string.server_refused_connection,
                    Toast.LENGTH_LONG);
            TextView message = (TextView) toast.getView().findViewById(android.R.id.message);
            message.setTextColor(Color.GRAY);
            toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        } else if (!succeded) {
            Toast toast = Toast.makeText(context, R.string.post_location_error, Toast.LENGTH_LONG);
            TextView message = (TextView) toast.getView().findViewById(android.R.id.message);
            message.setTextColor(Color.GRAY);
            toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, R.string.post_location_success,
                    Toast.LENGTH_LONG);
            TextView message = (TextView) toast.getView().findViewById(android.R.id.message);
            message.setTextColor(Color.GRAY);
            toast.getView().setBackgroundColor(Color.GREEN);
            toast.show();
        }
    }
}
