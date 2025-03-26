package com.example.digitalguardians;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class CallerInfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String timestamp = intent.getStringExtra("timestamp");

        if (phoneNumber != null) {
            Toast.makeText(context, "Call from: " + phoneNumber, Toast.LENGTH_LONG).show();

            // Store call details in SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("CallLogs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Append new call details
            String existingLogs = sharedPreferences.getString("logs", "");
            String newLog = phoneNumber + " at " + timestamp + "\n" + existingLogs;
            editor.putString("logs", newLog);
            editor.apply();
        }
    }
}
