package com.example.digitalguardians;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import java.util.concurrent.atomic.AtomicReference;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import android.util.Base64;
import android.os.Handler;
import android.util.Log;

public class SecureOTPHandler {
    private static final String KEY_ALIAS = "otp_encryption_key";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static Handler autoDeleteHandler = new Handler();
    private static final AtomicReference<String> encryptedOTP = new AtomicReference<>(null);

    public static void generateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            Log.d("SecureOTPHandler", "Generating new encryption key...");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(false)  // ✅ Disabled authentication for debugging
                    .build());
            keyGenerator.generateKey();
            Log.d("SecureOTPHandler", "Encryption key generated successfully.");
        } else {
            Log.d("SecureOTPHandler", "Encryption key already exists.");
        }
    }

    public static String encryptOTP(String otp) throws Exception {
        generateKey(); // Ensure key exists before encryption

        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);

        if (secretKey == null) {
            throw new Exception("Encryption key not found in Keystore!");
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();
        byte[] encryptedData = cipher.doFinal(otp.getBytes());

        String encryptedString = Base64.encodeToString(iv, Base64.DEFAULT) + ":" + Base64.encodeToString(encryptedData, Base64.DEFAULT);
        encryptedOTP.set(encryptedString);

        Log.d("SecureOTPHandler", "Encrypted OTP: " + encryptedString);
        Log.d("SecureOTPHandler", "IV Used: " + Base64.encodeToString(iv, Base64.DEFAULT));

        // Auto-delete OTP after 30 seconds
        autoDeleteHandler.postDelayed(() -> {
            encryptedOTP.set(null);
            Log.d("SecureOTPHandler", "Encrypted OTP auto-deleted.");
        }, 30000);

        return encryptedString;
    }

    public static String decryptOTP(String encryptedOTP) throws Exception {
        if (encryptedOTP == null || !encryptedOTP.contains(":")) {
            throw new IllegalArgumentException("Invalid OTP format");
        }

        String[] parts = encryptedOTP.split(":");
        byte[] iv = Base64.decode(parts[0], Base64.DEFAULT);
        byte[] encryptedData = Base64.decode(parts[1], Base64.DEFAULT);

        Log.d("SecureOTPHandler", "IV for decryption: " + Base64.encodeToString(iv, Base64.DEFAULT));

        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);

        if (secretKey == null) {
            throw new Exception("Decryption key not found in Keystore!");
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        String decryptedOTP = new String(cipher.doFinal(encryptedData));
        Log.d("SecureOTPHandler", "Decrypted OTP: " + decryptedOTP);

        return decryptedOTP;
    }
}
