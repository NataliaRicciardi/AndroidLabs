package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startCatImagesTask();

    }

    private void startCatImagesTask() {
        ImageView catImageView = findViewById(R.id.catImageView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        new CatImages(catImageView, progressBar).execute();
    }
}

class CatImages extends AsyncTask<String, Integer, String> {

    private ImageView imageView;
    private Bitmap catimg;
    private ProgressBar progressBar;

    public CatImages(ImageView imageView, ProgressBar progressBar) {
        this.imageView = imageView;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(String... strings) {

        while (true) {
            try {
                URL url = new URL("https://cataas.com/cat?json=true");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                int byteRead;

                while ((byteRead = inputStream.read()) != -1) {
                    response.append((char) byteRead);
                }
                connection.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String imageUrl = "https://cataas.com/cat/" + jsonResponse.getString("_id");
                String imageId = jsonResponse.getString("_id");

                // check if exists
                File imageFile = new File(imageView.getContext().getExternalFilesDir(null), imageId + ".jpg");
                if (imageFile.exists()) {
                    catimg = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                }
                else {
                    URL imageDownloadUrl = new URL(imageUrl);
                    HttpURLConnection imageConnection = (HttpURLConnection) imageDownloadUrl.openConnection();
                    imageConnection.setRequestMethod("GET");
                    InputStream imageInputStream = new BufferedInputStream(imageConnection.getInputStream());

                    // Save the image
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                    byte[] buffer = new byte[1024];
                    int bufferLength;
                    while ((bufferLength = imageInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, bufferLength);
                    }
                    fileOutputStream.close();
                    imageInputStream.close();

                    catimg = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                }

                for (int i = 0; i < 100; i++) {
                    try {
                        publishProgress(i);
                        Thread.sleep(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (catimg != null) {
            imageView.setImageBitmap(catimg);
        }
        progressBar.setProgress(values[0]);
    }
}