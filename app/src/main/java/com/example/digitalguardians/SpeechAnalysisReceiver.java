package com.example.digitalguardians;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SpeechAnalysisReceiver extends BroadcastReceiver {
    private static final String TAG = "SpeechAnalysisReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.e(TAG, "Received null intent");
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

        Log.d(TAG, "Received Speech Analysis Result: " + resultText);

        Toast.makeText(context, resultText, Toast.LENGTH_LONG).show();

        // Broadcast to update UI in MainActivity2
        Intent uiUpdateIntent = new Intent("SPEECH_ANALYSIS_RESULT");
        uiUpdateIntent.putExtra("CALL_RESULT", callTranscript);
        uiUpdateIntent.putExtra("IS_FRAUD", isFraud);
        LocalBroadcastManager.getInstance(context).sendBroadcast(uiUpdateIntent);
    }
}
