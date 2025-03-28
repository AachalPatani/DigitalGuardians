# 📞 Digital Guardians – Fraud Prevention & Security Suite

&#x20;&#x20;

## 🚀 Overview

**Digital Guardians** is an advanced **fraud prevention and mobile security** solution that combats scam calls, SIM swap fraud, OTP interception, and phishing attacks. Built using **Android BroadcastReceivers, encryption, and real-time fraud detection APIs**, it provides seamless, lightweight security without draining device resources.

## 🔥 Features

✅ **Call Fraud Detection** – Detects scam calls in real-time using external fraud APIs.\
✅ **OTP Security & Encryption** – Extracts and encrypts OTPs using **Android Keystore**.\
✅ **SIM Swap Detection** – Alerts users if a SIM swap occurs (ICCID tracking).\
✅ **Phishing Prevention** – Extracts URLs from SMS and scans for malicious content.\
✅ **Lightweight & Fast** – Uses **BroadcastReceivers** instead of background services.

## 📸 Screenshots
![WhatsApp Image 2025-03-27 at 19 47 54_af145239](https://github.com/user-attachments/assets/1fca2946-ea76-4588-bc58-7ba791b72ecf)
![WhatsApp Image 2025-03-27 at 19 47 54_bac94cb7](https://github.com/user-attachments/assets/87d6c1c5-8531-4712-b1f3-1fd07cf8dedb)
![WhatsApp Image 2025-03-27 at 19 47 53_49a06c79](https://github.com/user-attachments/assets/fcc4549a-230d-44be-a750-31a4e2b9ee54)



## 🏗️ Tech Stack

- **Languages:** Java, XML
- **Security:** AES Encryption (CBC Mode), Android Keystore
- **APIs:** Apilayer (Fraud Detection), Google Safe Browsing (Phishing Detection)
- **Android Components:** BroadcastReceivers, TelephonyManager, SharedPreferences

## 🎯 How It Works

1️⃣ **Detects Fraudulent Calls:** Intercepts incoming calls and verifies them against a fraud detection API.\
2️⃣ **Secures OTPs:** Extracts OTPs via SMS, encrypts them, and auto-deletes after 30 seconds.\
3️⃣ **Detects SIM Swaps:** Monitors SIM serial (ICCID) and alerts if changed.\
4️⃣ **Prevents Phishing Attacks:** Extracts SMS URLs and scans for malware/phishing threats.

## 🛠️ Installation & Setup

### **🔹 Prerequisites**

- Android Studio (Latest Version)
- Java 17+ (JDK 17)
- API Key for Apilayer (Replace in `CallListenerService.java`)

### **🔹 Steps to Run**

```bash
# Clone the repository
git clone https://github.com/your-username/DigitalGuardians.git
cd DigitalGuardians

# Open in Android Studio & Run on a Device/Emulator
```

## 📜 Permissions Required

This app requires the following Android permissions:

- **READ\_PHONE\_STATE** – To detect incoming calls
- **RECEIVE\_SMS** – To extract OTP messages
- **READ\_CALL\_LOG** – For call fraud detection
- **POST\_NOTIFICATIONS** – To alert users about fraud

## 🤝 Contribution Guidelines

We welcome contributions! Please follow these steps:
1️⃣ Fork the repository.\
2️⃣ Create a new branch (`feature/new-feature`).\
3️⃣ Commit your changes (`git commit -m "Added new feature"`).\
4️⃣ Push to the branch (`git push origin feature/new-feature`).\
5️⃣ Create a Pull Request.

## 📄 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

## 🌟 Acknowledgments

- Apilayer for fraud detection API
- Android Developers Community

**🚀 Let's build a safer digital world together!**

✅ **Fraud SIM Swap Detection** – Monitors SIM serial changes (ICCID) and alerts users to potential fraud attempts.

also trainned models of messages,urls are required. here you need to change host address and also phone n laptop must be in same network.

