package com.example.digitalguardians;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handles backend API communication and alerts/toasts based on results.
 */
public class ApiService {

    // Function to show Alert Dialog for high risk
    private static void showAlert(Context context, String title, String message) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true)
                    .show();
        });
    }


    // ✅ Handle response parsing and actions
    // ✅ Dynamic Response Handling
    private static void handleResponse(Context context, String prediction, String confidence, String source) {
        try {
            if (confidence == null || confidence.isEmpty()) {
                confidence = "0";  // Default to 0 if empty
            }

            try {
                String confidenceStr = confidence.replace("%", "").trim(); // Remove %
                double confidenceValue = Double.parseDouble(confidenceStr); // Convert safely
                Log.d("CONFIDENCE_SCORE", "Prediction: " + prediction + ", Confidence: " + confidenceValue);

                // Alert with confidence score
                showAlert(context, "🔍 Message Analysis",
                        "📩 Source: " + source + "\n" +
                                "📝 Prediction: " + prediction + "\n" +
                                "📊 Confidence: " + confidenceValue + "%");

                // Categorized alerts
                if (confidenceValue >= 80.0) {
                    showAlert(context, "⚠️ HIGH RISK DETECTED",
                            "🚨 This message from " + source + " is likely dangerous!\nConfidence: " + confidenceValue + "%");
                } else if (confidenceValue >= 60.0) {
                    showAlert(context, "⚠️ MODERATE RISK",
                            "⚠️ This content might be unsafe.\nConfidence: " + confidenceValue + "%");
                } else if (confidenceValue >= 40.0) {
                    showAlert(context, "⚠️ LOW RISK",
                            "⚠️ Might be slightly suspicious. Stay cautious.\nConfidence: " + confidenceValue + "%");
                } else {
                    showAlert(context, "✅ SAFE MESSAGE", "This message appears safe.\nConfidence: " + confidenceValue + "%");
                }
            } catch (NumberFormatException e) {
                Log.e("CONFIDENCE_ERROR", "Error parsing confidence: " + e.getMessage());
                Toast.makeText(context, "Error parsing confidence score", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    // ✅ Handle URL API response dynamically
    private static void handleUrlResponse(Context context, String prediction, String confidence, String url) {
        try {
            if (confidence == null || confidence.isEmpty()) {
                confidence = "0";  // Default to 0 if empty
            }

            try {
                String confidenceStr = confidence.replace("%", "").trim(); // Remove %
                double confidenceValue = Double.parseDouble(confidenceStr); // Convert safely
                Log.d("CONFIDENCE_SCORE", "Prediction: " + prediction + ", Confidence: " + confidenceValue);

                if (prediction.equalsIgnoreCase("fraud")) {
                    if (confidenceValue >= 80.0) {
                        showAlert(context, "⚠️ HIGH RISK URL",
                                "🚨 This URL may be dangerous!\nConfidence: " + confidenceValue + "%");
                    } else if (confidenceValue >= 50.0) {
                        showAlert(context, "⚠️ MODERATE RISK URL",
                                "⚠️ Be cautious! This URL might not be safe.\nConfidence: " + confidenceValue + "%");
                    } else {
                        showAlert(context, "⚠️ LOW RISK URL",
                                "⚠️ Might be slightly suspicious. Proceed with caution.\nConfidence: " + confidenceValue + "%");
                    }
                } else {
                    showAlert(context, "✅ SAFE URL", "This URL appears safe.\nConfidence: " + confidenceValue + "%");
                }
            } catch (NumberFormatException e) {
                Log.e("CONFIDENCE_ERROR", "Error parsing confidence: " + e.getMessage());
                Toast.makeText(context, "Error parsing confidence score", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





    // ✅ API call for SMS detection
    public static void sendSmsToBackend(Context context, String message, String sender) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.102:5000/")  // SMS API Server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoint api = retrofit.create(ApiEndpoint.class);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));

        api.predict(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("API_RESPONSE", "Raw Response: " + responseBody); // 🔍 Debug log

                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String prediction = jsonResponse.optString("prediction", "Not Spam");  // Default to "Not Spam"
                        String confidence = jsonResponse.optString("confidence", "0%");

                        // ✅ Debugging: Log API response to check if it's always "fraud"
                        Log.d("API_RESPONSE", "Prediction: " + prediction + ", Confidence: " + confidence);

                        // ✅ Handle response
                        handleResponse(context, prediction, confidence, "SMS from " + sender);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error parsing SMS response.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "SMS Check Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "SMS API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ API call for URL detection
    // ✅ API call for URL detection
    public static void sendUrlToBackend(Context context, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.102:5001/")  // Adjust URL API Server if needed
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoint api = retrofit.create(ApiEndpoint.class);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));

        api.predict(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String prediction = jsonResponse.getString("prediction");
                        String confidence = jsonResponse.getString("confidence");

                        // ✅ Handle the response dynamically
                        handleUrlResponse(context, prediction, confidence, url);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error parsing URL response.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "URL Check Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "URL API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
