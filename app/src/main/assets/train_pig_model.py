import pandas as pd
import numpy as np
import json
from sklearn.preprocessing import LabelEncoder
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.utils import to_categorical
import tensorflow as tf

# Load CSV
df = pd.read_csv("pig_temp_advice.csv")
X = df[['temperature']].values.astype('float32')
y = df['advice']

# Encode labels
le = LabelEncoder()
y_encoded = le.fit_transform(y)
y_categorical = to_categorical(y_encoded)

# Build model
model = Sequential()
model.add(Dense(32, input_shape=(1,), activation='relu'))   # Increased neurons
model.add(Dense(64, activation='relu'))                     # Added more depth
model.add(Dense(32, activation='relu'))                     # Extra layer for complexity
model.add(Dense(y_categorical.shape[1], activation='softmax'))

model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

model.fit(X, y_categorical, epochs=200, verbose=1)  # More training epochs


# Save as .tflite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
with open('pig_temp_model.tflite', 'wb') as f:
    f.write(tflite_model)

# Save label map
label_map = {int(k): v for k, v in zip(le.transform(le.classes_), le.classes_)}

with open('label_map.json', 'w') as f:
    json.dump(label_map, f)

print("✅ Model and label map saved successfully.")
