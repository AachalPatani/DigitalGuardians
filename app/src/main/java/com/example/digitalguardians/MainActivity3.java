package com.example.digitalguardians;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    private TextView callLogTextView;
    private String lastDisplayedCallerInfo = ""; // Stores the last displayed call info

    private final BroadcastReceiver callReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.e(TAG, "Received null intent in callReceiver");
                return;
            }

            String callerDetails = intent.getStringExtra("callerDetails");

            if (callerDetails != null) {
                // ✅ Prevent duplicate display
                if (!callerDetails.equals(lastDisplayedCallerInfo)) {
                    lastDisplayedCallerInfo = callerDetails; // Update last displayed
                    callLogTextView.append("\n" + callerDetails);
                    Log.d(TAG, "Caller details updated: " + callerDetails);
                } else {
                    Log.d(TAG, "Duplicate call details ignored.");
                }
            } else {
                Log.e(TAG, "Caller details missing in intent extras");
            }
        }
    };

    private final BroadcastReceiver speechAnalysisReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.e(TAG, "Received null intent in speechAnalysisReceiver");
                return;
            }

            String callTranscript = intent.getStringExtra("CALL_RESULT");
            boolean isFraud = intent.getBooleanExtra("IS_FRAUD", false);

            if (callTranscript == null) {
                Log.e(TAG, "Call transcript missing in intent extras");
                return;
            }

            String resultText = "🔊 Call Transcript:\n" + callTranscript +
                    "\n\n🚨 Analysis: " + (isFraud ? "❌ Fraud Detected!" : "✅ Safe Call");

            callLogTextView.append("\n" + resultText);
            Toast.makeText(context, resultText, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Speech Analysis Result updated in UI.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity3);

        callLogTextView = findViewById(R.id.callLogTextView);

        // Register receivers
        LocalBroadcastManager.getInstance(this).registerReceiver(callReceiver, new IntentFilter("CALLER_INFO"));
        LocalBroadcastManager.getInstance(this).registerReceiver(speechAnalysisReceiver, new IntentFilter("SPEECH_ANALYSIS_RESULT"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister receivers
        LocalBroadcastManager.getInstance(this).unregisterReceiver(callReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(speechAnalysisReceiver);
    }
}
