package android.hua.gr.gpstracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts an activity to show all the users and their locations.
     * @param v the view
     */
    public void fetchLocations(View v) {
        Intent intent = new Intent(this, LocationsService.class);
        startService(intent);
    }
}
