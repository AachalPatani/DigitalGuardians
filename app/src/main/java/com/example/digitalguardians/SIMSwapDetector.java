package com.example.digitalguardians;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import java.util.List;

public class SIMSwapDetector {
    private Context context;
    private static final String PREFS_NAME = "SimPrefs"; // SharedPreferences file name
    private static final String SIM_SERIAL_KEY = "sim_serial"; // Key for storing SIM Serial
    private static final String CHANNEL_ID = "SIM_SWAP_ALERT"; // Notification Channel ID
    private static final int PERMISSION_REQUEST_CODE = 100; // Unique request code for permissions

    public SIMSwapDetector(Context context) {
        this.context = context;
        requestPermissions(); // Request required permissions
        requestNotificationPermission(); // Ensure notification permissions are granted
        createNotificationChannel(); // Create notification channel for alerts
    }

    /**
     * Requests necessary permissions at runtime.
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6.0+ requires runtime permissions
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, "android.permission.READ_PRIVILEGED_PHONE_STATE") != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.READ_PHONE_STATE, "android.permission.READ_PRIVILEGED_PHONE_STATE"},
                        PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    /**
     * Retrieves the current SIM Serial Number.
     * Uses SubscriptionManager for Android 10+ due to security restrictions.
     *
     * @return SIM Serial Number (ICCID) or null if not accessible.
     */
    @SuppressLint("MissingPermission")
    public String getSimSerialNumber() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("SIMSwapDetector", "READ_PHONE_STATE permission not granted!");
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (subscriptionManager != null) {
                List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                if (subscriptionInfoList != null && !subscriptionInfoList.isEmpty()) {
                    return subscriptionInfoList.get(0).getIccId(); // ✅ Get ICC ID (SIM Serial)
                }
            }
            return null;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSimSerialNumber(); // For Android versions below 10
        }
    }

    /**
     * Checks if the SIM has been changed by comparing stored SIM serial with the current one.
     * If changed, raises an alert notification.
     *
     * @return Status message indicating whether SIM is up-to-date or swapped.
     */
    public String checkForSIMChange() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedSimSerial = preferences.getString(SIM_SERIAL_KEY, null);
        String currentSimSerial = getSimSerialNumber();

        if (currentSimSerial == null) {
            Log.e("SIMSwapDetector", "SIM serial number is null. Check permissions!");
            return "⚠️ Error: Unable to retrieve SIM data";
        }

        if (savedSimSerial != null && !savedSimSerial.equals(currentSimSerial)) {
            Log.e("SIM Swap Alert", "❌ Possible SIM swap detected!");
            showNotification("⚠️ SIM Swap Alert!", "Your SIM has been changed. If this wasn't you, contact support immediately!");
            return "❌ Possible SIM Swap Detected!";
        } else {
            preferences.edit().putString(SIM_SERIAL_KEY, currentSimSerial).apply();
            return "✅ SIM Status: Up-to-Date";
        }
    }

    /**
     * Simulates a fake SIM swap for hackathon demo purposes.
     * This method modifies the stored SIM serial, triggering a false positive detection.
     */
    public void simulateSimSwap() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(SIM_SERIAL_KEY, "FAKE_SIM_123456").apply(); // Fake SIM ID
        Log.d("SIMSwapDetector", "Simulated SIM Swap triggered!");
    }

    /**
     * Resets the stored SIM serial to the current SIM's actual serial.
     * This brings the status back to "SIM Status: Up-to-Date".
     */
    public void resetSimStatus() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String actualSimSerial = getSimSerialNumber();

        if (actualSimSerial != null) {
            preferences.edit().putString(SIM_SERIAL_KEY, actualSimSerial).apply();
            Log.d("SIMSwapDetector", "SIM status reset to actual SIM.");
        } else {
            Log.e("SIMSwapDetector", "Failed to reset SIM status. Check permissions!");
        }
    }

    /**
     * Creates a notification channel required for sending SIM swap alerts (Android 8+).
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26+
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, "SIM Swap Alerts", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Notifies the user when a SIM swap is detected.");
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Displays a notification when a SIM swap is detected.
     *
     * @param title   Notification title.
     * @param message Notification message.
     */
    @SuppressLint("NotificationPermission")
    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.warning) // Ensure you have this icon in res/drawable
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    /**
     * Requests notification permissions for Android 13+ devices.
     */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33)
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101);
            }
        }
    }
}
