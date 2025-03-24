package com.example.digitalguardians;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

// BankTipsActivity class for displaying fraud prevention tips
public class BankTipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_tips);

        TextView tipsText = findViewById(R.id.tipsText);

        // Get all fraud prevention tips
        String allTips = BankSecurityTips.getAllTips();
        tipsText.setText(allTips);
    }
}

// Class containing various security tips
class BankSecurityTips {

    // Method to get all tips as a formatted string
    public static String getAllTips() {
        StringBuilder tipsBuilder = new StringBuilder();

        tipsBuilder.append("🔐 **OTP Safety**\n");
        for (String tip : OTPSecurity.getTips()) {
            tipsBuilder.append("• ").append(tip).append("\n");
        }

        tipsBuilder.append("\n💰 **Financial Security**\n");
        for (String tip : FinancialSecurity.getTips()) {
            tipsBuilder.append("• ").append(tip).append("\n");
        }

        tipsBuilder.append("\n🌐 **Online Banking Security**\n");
        for (String tip : OnlineBankingSecurity.getTips()) {
            tipsBuilder.append("• ").append(tip).append("\n");
        }

        tipsBuilder.append("\n💳 **Card Security**\n");
        for (String tip : CardSecurity.getTips()) {
            tipsBuilder.append("• ").append(tip).append("\n");
        }

        return tipsBuilder.toString();
    }
}

// OTP Safety Tips
class OTPSecurity {
    public static List<String> getTips() {
        return List.of(
                "Never share your OTP with anyone, even if they claim to be from the bank.",
                "Do not enter OTPs on suspicious websites or apps.",
                "Enable two-factor authentication (2FA) for extra security.",
                "Report any unauthorized OTP requests immediately."
        );
    }
}

// Financial Security Tips
class FinancialSecurity {
    public static List<String> getTips() {
        return List.of(
                "Regularly check your bank statements for suspicious transactions.",
                "Never share your account details or passwords with anyone.",
                "Use only official bank customer service numbers for inquiries.",
                "Set up SMS/email alerts for transactions."
        );
    }
}

// Online Banking Security Tips
class OnlineBankingSecurity {
    public static List<String> getTips() {
        return List.of(
                "Always verify the bank website URL before logging in.",
                "Avoid accessing banking services on public Wi-Fi networks.",
                "Use a strong and unique password for banking apps.",
                "Enable biometric authentication (fingerprint/face recognition) if available."
        );
    }
}

// Card Security Tips
class CardSecurity {
    public static List<String> getTips() {
        return List.of(
                "Never write down your card PIN; memorize it instead.",
                "Use virtual cards for online transactions when possible.",
                "Always cover the keypad when entering your PIN at ATMs or POS machines.",
                "Report lost or stolen cards immediately to your bank."
        );
    }
}
