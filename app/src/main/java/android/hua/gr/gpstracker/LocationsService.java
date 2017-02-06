package android.hua.gr.gpstracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

public class LocationsService extends Service {
    Context context;

    @Override
    public void onCreate() {
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        ConnectivityHelper connectivityHelper = new ConnectivityHelper(context);

        if (connectivityHelper.isNetworkAvailable())
            new FetchLocations(context).execute();
        else {
            Toast toast = Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG);
            TextView message = (TextView) toast.getView().findViewById(android.R.id.message);
            message.setTextColor(Color.GRAY);
            toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
