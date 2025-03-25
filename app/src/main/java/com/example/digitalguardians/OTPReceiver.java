package com.example.digitalguardians;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPReceiver extends BroadcastReceiver {
    private static OTPListener otpListener;

    public static void setOTPListener(OTPListener listener) {
        otpListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("OTPReceiver", "SMS received");

        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Log.d("OTPReceiver", "Intent matched SMS_RECEIVED");

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String messageBody = smsMessage.getMessageBody();
                        Log.d("OTPReceiver", "Received SMS: " + messageBody);

                        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
                        Matcher matcher = pattern.matcher(messageBody);

                        if (matcher.find()) {
                            String otp = matcher.group(0);
                            Log.d("OTPReceiver", "Extracted OTP: " + otp);

                            if (otpListener != null) {
                                otpListener.onOTPReceived(otp);
                            }
                        } else {
                            Log.d("OTPReceiver", "No OTP found in message");
                        }
                    }
                } else {
                    Log.d("OTPReceiver", "PDU is null, no SMS received");
                }
            } else {
                Log.d("OTPReceiver", "Bundle is null, no SMS data");
            }
        }
    }

    public interface OTPListener {
        void onOTPReceived(String otp);
    }
}
