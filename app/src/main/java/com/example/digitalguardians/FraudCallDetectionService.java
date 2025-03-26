package com.example.digitalguardians;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class FraudCallDetectionService extends Service {
    private MediaRecorder recorder;
    private String phoneNumber;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phoneNumber = intent.getStringExtra("incomingNumber");
        startRecording();
        return START_STICKY;
    }

    private void startRecording() {
        File audioFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "fraud_call_" + phoneNumber + ".mp3");

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            recorder.prepare();
            recorder.start();
            showToast("Recording Started: " + phoneNumber);
        } catch (IOException e) {
            showToast("Recording Failed: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            showToast("Recording Stopped");
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}