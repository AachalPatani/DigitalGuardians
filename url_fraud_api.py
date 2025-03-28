import pickle
import pandas as pd
import numpy as np
from flask import Flask, request, jsonify
from sklearn.feature_extraction.text import TfidfVectorizer  # ✅ Needed for transformation

# ✅ Initialize Flask app
app = Flask(__name__)

# ✅ Load trained model and vectorizer
model_path = r'D:\FraudDetection1\url_model.pkl'
vectorizer_path = r'D:\FraudDetection1\url_vectorizer.pkl'  # Load vectorizer

try:
    model = pickle.load(open(model_path, 'rb'))
    vectorizer = pickle.load(open(vectorizer_path, 'rb'))  # Load TF-IDF Vectorizer
    print("✅ Model and Vectorizer loaded successfully!")
except Exception as e:
    print(f"❌ Error loading model/vectorizer: {e}")

# ✅ Home route for test
@app.route('/')
def home():
    return "🚀 URL Fraud Detection API is running!"

# ✅ Prediction API
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # ✅ Get URL from POST request
        data = request.get_json(force=True)
        url = data.get('url', '').strip()

        if not url:
            return jsonify({'error': 'No URL provided'}), 400

        # ✅ Transform URL using TF-IDF Vectorizer
        url_vector = vectorizer.transform([url])
        
        # ✅ Model prediction
        prediction = model.predict(url_vector)[0]
        probabilities = model.predict_proba(url_vector)[0]

        # ✅ Confidence calculation
        fraud_confidence = round(probabilities[1] * 100, 2)  # Probability of fraud

        # ✅ Prepare JSON response
        response = {
            'prediction': 'fraud' if prediction == 0 else 'safe',  # 1 = Fraud, 0 = Safe
            'confidence': f"{fraud_confidence}%"
        }

        return jsonify(response), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 500

# ✅ Run app on all available IP addresses so it works on mobile & PC
if __name__ == '__main__':
    app.run(host='192.168.200.30', port=5001, debug=True)  # ✅ Correct way to expose API to local network
