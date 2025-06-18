package com.example.pigfarmmanagementapp;

import android.content.Context;

import org.json.JSONException;
import org.tensorflow.lite.Interpreter;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class PigAdvicePredictor {
    private Interpreter interpreter;
    private HashMap<Integer, String> labelMap;

    public PigAdvicePredictor(Context context) throws IOException, JSONException {
        interpreter = new Interpreter(loadModelFile(context));
        labelMap = loadLabelMap(context);
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        FileInputStream inputStream = new FileInputStream(context.getAssets().openFd("pig_temp_model.tflite").getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = context.getAssets().openFd("pig_temp_model.tflite").getStartOffset();
        long declaredLength = context.getAssets().openFd("pig_temp_model.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private HashMap<Integer, String> loadLabelMap(Context context) throws IOException, JSONException {
        HashMap<Integer, String> map = new HashMap<>();
        InputStream is = context.getAssets().open("label_map.json");
        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        String json = scanner.hasNext() ? scanner.next() : "";
        JSONObject object = new JSONObject(json);
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(Integer.parseInt(key), object.getString(key));
        }
        return map;
    }


        public String getAdvice(float temperature) {
            System.out.println("Running model with temp: " + temperature);
            
            float[][] input = {{ temperature }};
            float[][] output = new float[1][labelMap.size()];
            interpreter.run(input, output);
    
            int predictedIndex = 0;
            float max = output[0][0];
            for (int i = 1; i < output[0].length; i++) {
                System.out.println("Index " + i + " = " + output[0][i]);
                if (output[0][i] > max) {
                    max = output[0][i];
                    predictedIndex = i;
                }
            }
            System.out.println("Predicted index: " + predictedIndex);
            System.out.println("Advice: " + labelMap.get(predictedIndex));
    
            return labelMap.get(predictedIndex);
        }
}
