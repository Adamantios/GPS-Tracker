package android.hua.gr.gpstracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

public class GPSTracker extends Service {

    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 30 * 1000;
    private static final float LOCATION_DISTANCE = 0;

    private class LocationListener implements android.location.LocationListener {
        Location lastLocation;

        LocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            lastLocation.set(location);
            ConnectivityHelper connectivityHelper = new ConnectivityHelper(getApplicationContext());

            if (connectivityHelper.isNetworkAvailable())
                new RegisterLocation(lastLocation, getApplicationContext()).execute();
            else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.network_error,
                        Toast.LENGTH_LONG);
                TextView message = (TextView) toast.getView().findViewById(android.R.id.message);
                message.setTextColor(Color.GRAY);
                toast.getView().setBackgroundColor(Color.RED);
                toast.show();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);
        } catch (SecurityException | IllegalArgumentException ignored) {
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (SecurityException | IllegalArgumentException ignored) {
        }
    }

    private void initializeLocationManager() {
        if (locationManager == null)
            locationManager = (LocationManager)
                    getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }
}
