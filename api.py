from flask import Flask, request, jsonify
import pickle
import os

# ✅ Initialize Flask app
app = Flask(__name__)

# ✅ Load model & vectorizer dynamically
model_path = os.path.join(os.path.dirname(__file__), 'spam_model.pkl')
vectorizer_path = os.path.join(os.path.dirname(__file__), 'vectorizer.pkl')

try:
    model = pickle.load(open(model_path, 'rb'))
    vectorizer = pickle.load(open(vectorizer_path, 'rb'))
    print("✅ Model & Vectorizer Loaded Successfully!")
except FileNotFoundError:
    print("⚠️ Model or Vectorizer file not found! Check the paths.")

# ✅ API Home Route
@app.route('/')
def home():
    return jsonify({'status': 'API is running', 'message': 'Send POST requests to /predict'}), 200

# ✅ Spam Prediction API Endpoint
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # ✅ Get message from JSON body
        data = request.get_json(force=True)
        message = data.get('message', '').strip()

        if not message:
            return jsonify({'error': 'No message provided for prediction.'}), 400

        # ✅ Vectorize message & Predict
        message_vec = vectorizer.transform([message])
        probabilities = model.predict_proba(message_vec)[0]  # [prob_ham, prob_spam]

        risk_score = probabilities[1] * 100  # Spam probability as confidence score
        threshold = 70  # ✅ Mark as spam if confidence is 60% or higher

        prediction = "spam" if risk_score >= threshold else "Not Spam"

        # ✅ Response JSON
        response = {
            'prediction': prediction,
            'confidence': f"{risk_score:.2f}%"  # Confidence score
        }
        return jsonify(response), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 500

# ✅ Run Flask API
if __name__ == '__main__':
    

    app.run(host='192.168.200.30', port=5000, debug=True)
