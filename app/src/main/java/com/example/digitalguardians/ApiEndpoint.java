package com.example.digitalguardians;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API interface for backend communication (SMS, URL, Call).
 *
 * All three endpoints (SMS, URL, Call detection)
 * follow the same /predict API format.
 */
public interface ApiEndpoint {

    /**
     * Generic predict endpoint for SMS, URL, and Call fraud detection.
     *
     * Request JSON:
     * {
     *     "message": "text message here"  // for SMS
     * }
     *
     * OR
     *
     * {
     *     "url": "http://example.com"  // for URL
     * }
     *
     * OR
     *
     * {
     *     "number": "+14155552671"  // for Call number
     * }
     *
     * Response JSON:
     * {
     *     "prediction": "fraud",  // or "spam" / "safe"
     *     "confidence": "92.55%"
     * }
     */
    @POST("predict")
    Call<ResponseBody> predict(@Body RequestBody body);
}
