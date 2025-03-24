package com.example.digitalguardians;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private TextView chatView;
    private EditText userInput;
    private Button sendButton, voiceButton;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private TextToSpeech tts;
    private Map<String, String> responseMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView = findViewById(R.id.chatView);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);
        voiceButton = findViewById(R.id.voiceButton);

        initializeResponses();
        checkAndRequestPermissions();

        // Initialize TextToSpeech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
            }
        });

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(ChatActivity.this, "Listening...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                Toast.makeText(ChatActivity.this, "Processing...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error) {
                Toast.makeText(ChatActivity.this, "Speech Recognition Error: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    processUserInput(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        sendButton.setOnClickListener(v -> processUserInput(userInput.getText().toString()));

        voiceButton.setOnClickListener(v -> {
            Log.d("VoiceButton", "Voice Button Clicked");
            startVoiceRecognition();
        });
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Microphone Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Microphone Permission Required for Voice Input", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeResponses() {
        responseMap = new HashMap<>();
        responseMap.put("hello", "Hello! How can I assist you?");
        responseMap.put("bank", "Bank account kholne ke liye, apna ID proof aur address proof le kar bank jaayein.");
        responseMap.put("fraud", "Aap turant apne bank se sampark karein aur fraud report karein.");
        responseMap.put("cyber crime", "Aap www.cybercrime.gov.in par jakar ya 1930 par call karke report kar sakte hain.");
        responseMap.put("secure", "Hamesha two-factor authentication ka upayog karein.");
        responseMap.put("phishing", "Phishing ek tarika hai jisme fraudsters aapki personal details churaane ki koshish karte hain. Hamesha suspicious links aur emails se savdhaan rahein.");
        responseMap.put("phishing attack", "Beware of phishing emails that ask for personal information. Always verify the sender before clicking on any link.");
        responseMap.put("OTP fraud", "Kisi bhi unknown vyakti ke saath OTP share na karein. Bank kabhi bhi OTP nahi maangta.");
        responseMap.put("password security", "Strong password banane ke liye uppercase, lowercase, numbers aur special characters ka upyog karein.");
        responseMap.put("strong password", "Create strong passwords using a mix of uppercase, lowercase, numbers, and special characters.");
        responseMap.put("data breach", "Data breach hone par turant apne passwords badlein aur 2FA enable karein.");
        responseMap.put("two-factor authentication", "2FA lagane se aapka account zyada surakshit rehta hai. Hamesha enable karein.");
        responseMap.put("bank fraud", "Agar aapke account se bina aapki anumati ke paisa kata gaya hai to turant bank se sampark karein.");
        responseMap.put("dark web", "Dark web ek hidden internet space hai jisme illegal activities hoti hain. Public networks par yahaan jaana risky hai.");
        responseMap.put("ransomware attack", "Ransomware ek malware hai jo aapke data ko encrypt kar deta hai aur fir ransom demand karta hai. Hamesha apni files ka backup rakhein.");
        responseMap.put("identity theft", "Identity theft hone se bachne ke liye aapke personal documents aur details kisi ke saath share na karein.");
        responseMap.put("public WiFi risk", "Public WiFi networks ka upyog karne se aapke data ko hackers intercept kar sakte hain. VPN ka upyog karein.");
        responseMap.put("public Wi-Fi risk", "Public WiFi networks ka upyog karne se aapke data ko hackers intercept kar sakte hain. VPN ka upyog karein.");
        responseMap.put("cybercrime complaint", "Agar aap cybercrime ke shikar hue hain to www.cybercrime.gov.in par report karein ya 1930 par call karein.");
        responseMap.put("social media safety", "Social media par apni personal information public na karein aur privacy settings ko hamesha review karein.");
        responseMap.put("safe browsing", "Hamesha HTTPS websites ka upyog karein aur ad-blockers enable karein.");
        responseMap.put("scam call", "Agar koi aapse bank ya personal details phone par maang raha hai to turant call kaat dein.");
        responseMap.put("malware protection", "Anti-virus software install karein aur apne devices ko regular update karein.");
        responseMap.put("email security", "Agar koi unknown email attachment hai to use bina scan kiye open na karein.");
        responseMap.put("AI in cybersecurity", "AI ka upyog automated threat detection aur fraud prevention ke liye kiya jata hai.");
        responseMap.put("VPN kya hai", "VPN ek tool hai jo aapka internet connection encrypt karta hai aur aapki privacy badhata hai.");
        responseMap.put("blockchain security", "Blockchain decentralization aur cryptographic hashing ka upyog karke data security badhata hai.");
        responseMap.put("ethical hacking", "Ethical hacking ka matlab hai authorized hacking jisse systems ke vulnerabilities detect ki ja sakein.");
        responseMap.put("bug bounty", "Bug bounty ek program hai jisme companies hackers ko unke systems ki vulnerabilities dhundhne ke liye paise deti hain.");

        responseMap.put("cyber security career", "Agar aapko cybersecurity mein career banana hai to aapko ethical hacking aur network security seekhna hoga.");
        responseMap.put("cybersecurity career", "Agar aapko cybersecurity mein career banana hai to aapko ethical hacking aur network security seekhna hoga.");
        responseMap.put("dark web safe hai kya", "Dark web par jane ke liye TOR browser ka upyog hota hai, lekin yeh illegal aur risky ho sakta hai.");
        responseMap.put("how to avoid cyber fraud", "Cyber fraud se bachne ke liye apni personal details kisi ke saath share na karein aur hamesha alerts enable rakhein.");
        responseMap.put("IoT security", "IoT devices ko hack hone se bachane ke liye strong passwords aur firmware updates ka upyog karein.");
        responseMap.put("AI in hacking", "AI ka upyog hacking aur cybersecurity dono mein ho raha hai, AI-based threats aur AI-based protection dono important hain.");
        responseMap.put("fraud prevention", "Fraud hone se bachne ke liye hamesha two-factor authentication aur secure payment methods ka upyog karein.");
        responseMap.put("zero trust security", "Zero Trust model me har request verify hoti hai, chahe wo internal ho ya external.");
        responseMap.put("honeypot in cybersecurity", "Honeypot ek security mechanism hai jo attackers ko trap karne aur unki activities monitor karne ke liye use hota hai.");
        responseMap.put("financial fraud", "Agar aap financial fraud ka shikar hue hain, to turant apne bank se sampark karein aur cybercrime portal par complaint darj karein.");
        responseMap.put("card fraud", "Agar aapke card se bina aapki anumati ke transaction hua hai to turant card block karein aur bank se sampark karein.");
        responseMap.put("UPI fraud", "UPI fraud se bachne ke liye kisi ke saath PIN share na karein aur sirf trusted sources se payment karein.");
        responseMap.put("loan scam", "Agar aapko bina kisi application ke loan approval ka message aaye to yeh fraud ho sakta hai.");
        responseMap.put("investment scam", "Bahut zyada return ka wada karne wale investment schemes fraud ho sakti hain, hamesha SEBI registered companies ko hi trust karein.");
        responseMap.put("SIM swap fraud", "Agar aapka SIM card band ho gaya hai aur bank se OTP nahi aa raha hai to turant network provider se sampark karein.");
        responseMap.put("fake customer care", "Koi bhi customer care number Google se na dekhein, hamesha official website par jaakar check karein.");
        responseMap.put("QR code fraud", "Kisi anjaan QR code ko scan na karein, fraudsters aapke account se paisa nikal sakte hain.");
        responseMap.put("fake job scam", "Agar kisi job offer me pehle paisa maanga ja raha hai to yeh fraud ho sakta hai, hamesha verify karein.");
        responseMap.put("cyber police", "Cyber fraud report karne ke liye aap apne najdeeki cyber police station ya www.cybercrime.gov.in par complaint darj kar sakte hain.");

        responseMap.put("cyber crime complaint", "Agar aap cybercrime ke shikar hue hain to www.cybercrime.gov.in par report karein ya 1930 par call karein.");
    }

    private void processUserInput(String input) {
        if (input.trim().isEmpty()) return;

        String response = getResponseForInput(input.toLowerCase());

        // Show response in chat
        chatView.append("\nYou: " + input);
        chatView.append("\nBot: " + response);

        // Speak response
        tts.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);

        userInput.setText("");
    }

    private String getResponseForInput(String input) {
        for (Map.Entry<String, String> entry : responseMap.entrySet()) {
            if (input.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Sorry, I don't understand. Can you rephrase?";
    }

    private void startVoiceRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }

        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechRecognizerIntent);
        } else {
            Toast.makeText(this, "Speech Recognizer not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
