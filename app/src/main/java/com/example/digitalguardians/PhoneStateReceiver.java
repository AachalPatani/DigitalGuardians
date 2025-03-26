package com.example.digitalguardians;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                Toast.makeText(context, "Incoming call from: " + phoneNumber, Toast.LENGTH_LONG).show();

                // Send broadcast with caller details
                Intent broadcastIntent = new Intent("CALLER_INFO");
                broadcastIntent.putExtra("callerDetails", "Call from: " + phoneNumber + "\nTime: " + timestamp);

                // Use LocalBroadcastManager to send data to MainActivity2
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            }
        }
    }
}
