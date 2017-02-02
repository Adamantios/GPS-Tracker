package android.hua.gr.gpstracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

class LocationsService extends Service{
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
        ConnectivityHelper connectivityHelper = new ConnectivityHelper(getApplicationContext());

        if (connectivityHelper.isNetworkAvailable())
            new FetchLocations(getApplicationContext()).execute();
        else
            connectivityHelper.showNetworkAlert();
    }
}
