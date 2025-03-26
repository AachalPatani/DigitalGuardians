package com.example.digitalguardians;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String callerDetails = intent.getStringExtra("callerDetails");
        if (callerDetails != null) {
            Toast.makeText(context, "📞 Caller: " + callerDetails, Toast.LENGTH_LONG).show();
        }
    }
}
