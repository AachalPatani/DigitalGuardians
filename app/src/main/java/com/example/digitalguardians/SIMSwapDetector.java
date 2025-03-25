package com.example.digitalguardians;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SIMSwapDetector {
    private Context context;
    private static final String PREFS_NAME = "SimPrefs";
    private static final String SIM_SERIAL_KEY = "sim_serial";
    private static final String CHANNEL_ID = "SIM_SWAP_ALERT";

    public SIMSwapDetector(Context context) {
        this.context = context;
        requestNotificationPermission();
        createNotificationChannel();
    }

    public String getSimSerialNumber() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("SIMSwapDetector", "READ_PHONE_STATE permission not granted!");
            return null;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }

    public void checkForSIMChange() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedSimSerial = preferences.getString(SIM_SERIAL_KEY, null);
        String currentSimSerial = getSimSerialNumber();

        if (currentSimSerial == null) {
            Log.e("SIMSwapDetector", "SIM serial number is null. Check permissions!");
            return;
        }

        if (savedSimSerial != null && !savedSimSerial.equals(currentSimSerial)) {
            Log.e("SIM Swap Alert", "Possible SIM swap detected!");
            showNotification("⚠️ SIM Swap Alert!", "Your SIM has been changed. If this wasn't you, contact support immediately!");
        } else {
            preferences.edit().putString(SIM_SERIAL_KEY, currentSimSerial).apply();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // ✅ Fix: Only create channel if API level is 26+
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, "SIM Swap Alerts", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Notifies the user when a SIM swap is detected.");
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @SuppressLint("NotificationPermission")
    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.warning) // ✅ Ensure you have this icon in res/drawable
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33)
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        (MainActivity2) context,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101);
            }
        }
    }
}
