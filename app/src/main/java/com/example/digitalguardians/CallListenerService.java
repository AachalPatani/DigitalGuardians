package com.example.digitalguardians;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallListenerService extends BroadcastReceiver {
    private static final String TAG = "CallListenerService";
    private static final String API_KEY = "0af9fef2fa178d372ed956b3f0706ef8"; // 🔹 Replace with actual API key
    private static final String API_URL = "http://apilayer.net/api/validate?access_key=" + API_KEY + "&number=";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "Received null intent or action");
            return;
        }

        if (!TelephonyManager.EXTRA_STATE_RINGING.equals(intent.getStringExtra(TelephonyManager.EXTRA_STATE))) {
            return;
        }

        // 🔹 Check if app has necessary permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission denied: READ_PHONE_STATE or READ_CALL_LOG");
            return;
        }

        // 🔹 Get incoming number
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (incomingNumber != null) {
            Log.d(TAG, "Incoming call from: " + incomingNumber);
            new FraudCheckTask(context).execute(incomingNumber);
        } else {
            Log.e(TAG, "Incoming number is null");
        }
    }

    /**
     * 🔹 Background task to check fraud number via API
     */
    private static class FraudCheckTask extends AsyncTask<String, Void, String> {
        private final Context context;

        public FraudCheckTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String phoneNumber = params[0];
            String apiUrl = API_URL + phoneNumber;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();
            } catch (Exception e) {
                Log.e(TAG, "API request failed: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                showToast(context, "❌ API Error: Could not check number");
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean isFraud = jsonResponse.optBoolean("fraud", false);
                String phoneNumber = jsonResponse.optString("number", "Unknown");

                String message = isFraud ? "⚠ Fraud Alert! " + phoneNumber : "✅ Safe Caller: " + phoneNumber;
                showToast(context, message);

                // 🔹 Broadcast fraud result to update UI
                Intent fraudIntent = new Intent("CALLER_INFO");
                fraudIntent.putExtra("callerDetails", message);
                LocalBroadcastManager.getInstance(context).sendBroadcast(fraudIntent);

            } catch (Exception e) {
                Log.e(TAG, "Failed to parse API response: " + e.getMessage());
                showToast(context, "❌ API Error: Invalid response format");
            }
        }

        private void showToast(Context context, String message) {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            );
        }
    }
}
