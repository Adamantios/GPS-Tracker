package android.hua.gr.gpstracker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.Calendar;

public class GPSStartedReceiver extends BroadcastReceiver {

    /**
     * Waiting time until starting to track the user's location.
     */
    private static final long WAITING_TIME = 1000 * 5;

    /**
     * The tracking frequency in milliseconds.
     */
    private static final int TRACKING_FREQUENCY = 1000 * 30;

    /**
     * The fetching frequency for the other users and their locations in milliseconds.
     */
    private static final int FETCHING_FREQUENCY = 1000 * 60 * 10;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        // If providers have changed
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            ConnectivityHelper helper = new ConnectivityHelper(context);

            // If GPS is available
            if (helper.isGpsAvailable()) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.starting_tracker),
                        Toast.LENGTH_SHORT).show();

                // Start GPSTracker service after WAITING_TIME
                new CountDownTimer(WAITING_TIME, 5000) {
                    // When the countdown is over
                    @SuppressLint("ShortAlarm")
                    public void onFinish() {
                        Intent intent = new Intent(context, GPSTracker.class);
                        PendingIntent pendingIntent =
                                PendingIntent.getService(context, 0, intent, 0);

                        AlarmManager trackingAlarm =
                                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        trackingAlarm.setRepeating(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), TRACKING_FREQUENCY, pendingIntent);

                        intent = new Intent(context, LocationsService.class);
                        pendingIntent = PendingIntent.getService(context, 0, intent, 0);

                        AlarmManager fetchingAlarm =
                                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        fetchingAlarm.setRepeating(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), FETCHING_FREQUENCY, pendingIntent);
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }
        }
    }
}
