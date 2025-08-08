package com.example.pigfarmmanagementapp.handler;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageHandler {

    public static void uploadImageToCloudinary(Context context, Uri imageUri, OnImageUploadedListener listener) {
        Map config = new HashMap();
        config.put("cloud_name", "dkgy0tn0c");
        config.put("api_key", "767877425211721");
        config.put("api_secret", "nP9zEoRWfhKV_V9CLLr_Xux0gBM");

        Cloudinary cloudinary = new Cloudinary(config);

        try {
            File file = FileUtils.getFile(context, imageUri); // Use your own URI-to-File logic
            new Thread(() -> {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                    String imageUrl = (String) uploadResult.get("secure_url");

                    ((Activity) context).runOnUiThread(() -> listener.onUploaded(imageUrl));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnImageUploadedListener {
        void onUploaded(String imageUrl);
    }
}
