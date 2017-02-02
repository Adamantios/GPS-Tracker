package android.hua.gr.gpstracker;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

class ConnectivityHelper {

    private Context context;

    /**
     * ConnectivityHelper Constructor
     * @param context the context
     */
    ConnectivityHelper(Context context){
        this.context = context;
    }

    /**
     * Checks if network is available
     * @return if network is available
     */
    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Checks if GPS is available
     * @return if GPS is available
     */
    boolean isGpsAvailable() {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Shows Alert Dialog in order to get the user to the GPS settings
     */
    void showGPSAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.error));

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.GPS_settings));

        // Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // Cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Shows Alert Dialog in order to get the user to the network settings
     */
    void showNetworkAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.error));

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.internet_settings));

        // Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;

                if (currentApiVersion < Build.VERSION_CODES.JELLY_BEAN) {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    ComponentName cName = new ComponentName("com.android.phone",
                            "com.android.phone.Settings");
                    intent.setComponent(cName);
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                    context.startActivity(intent);
                }
            }
        });

        // Cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}