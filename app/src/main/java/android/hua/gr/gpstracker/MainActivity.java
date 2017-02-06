package android.hua.gr.gpstracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a progress dialog
        progressDialog = new ProgressDialog(this);
    }

    /**
     * Starts an activity to show all the users and their locations.
     *
     * @param v the view
     */
    public void fetchLocations(View v) {
        ConnectivityHelper connectivityHelper = new ConnectivityHelper(this);

        if (!connectivityHelper.isNetworkAvailable())
            connectivityHelper.showNetworkAlert();
        else {
            startProgressDialog();
            Intent intent = new Intent(this, LocationsService.class);
            this.startService(intent);
        }
    }

    private static void startProgressDialog() {
        progressDialog.setTitle("Downloading");
        progressDialog.setMessage("Downloading data...");
        // Disable dismiss by tapping outside of the dialog
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
