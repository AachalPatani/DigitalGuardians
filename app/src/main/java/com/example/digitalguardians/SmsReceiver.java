package com.example.digitalguardians;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Receives incoming SMS, extracts URL, and sends to backend API.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Context appContext = context.getApplicationContext(); // ✅ Use application-level context

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object pdu : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                String smsBody = message.getMessageBody();
                String sender = message.getOriginatingAddress();

                // Extract URL if present in SMS
                String url = Utils.extractUrl(smsBody);

                if (!url.isEmpty()) {
                    ApiService.sendUrlToBackend(appContext, url);
                }

                // ✅ Send SMS for checking
                ApiService.sendSmsToBackend(appContext, smsBody, sender);
            }
        }
    }
}