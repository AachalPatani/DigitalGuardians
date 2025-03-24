package com.example.digitalguardians;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.digitalguardians.R;

public class ReportFraudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fraud);

        EditText edtFraudDetails = findViewById(R.id.edtFraudDetails);
        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);

        btnSubmitReport.setOnClickListener(v -> {
            String reportText = edtFraudDetails.getText().toString().trim();
            if (!reportText.isEmpty()) {
                // 🚀 TODO: Send this report to your backend or save it locally for now
                Toast.makeText(this, "Fraud report submitted successfully!", Toast.LENGTH_SHORT).show();
                edtFraudDetails.setText(""); // Clear after submission
            } else {
                Toast.makeText(this, "Please enter fraud details before submitting.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

