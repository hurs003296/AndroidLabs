package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        new CatImages().execute();
    }

    private class CatImages extends AsyncTask<Void, Integer, Bitmap> {

        private Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(Void... voids) {
            while (true) {
                try {
                    // Fetch JSON data from the URL
                    URL url = new URL("https://cataas.com/cat?json=true");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    StringBuilder jsonBuilder = new StringBuilder();
                    int ch;
                    while ((ch = in.read()) != -1) {
                        jsonBuilder.append((char) ch);
                    }
                    String jsonString = jsonBuilder.toString();

                    // Parse JSON to get image ID and URL
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String imageId = jsonObject.getString("id");
                    String imageUrl = "https://cataas.com/cat/" + imageId;

                    // Check if the image file already exists
                    File imageFile = new File(getFilesDir(), imageId + ".jpg");
                    if (imageFile.exists()) {
                        // Load the image from the device
                        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    } else {
                        // Download the image and save it locally
                        URL imageDownloadUrl = new URL(imageUrl);
                        HttpURLConnection imageConnection = (HttpURLConnection) imageDownloadUrl.openConnection();
                        InputStream imageStream = imageConnection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(imageStream);

                        // Save the image to the device
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                    }

                    // Update the progress bar
                    for (int i = 0; i < 100; i++) {
                        publishProgress(i);
                        Thread.sleep(30); // Adjust the speed if needed
                    }

                    // Return the bitmap to be displayed
                    return bitmap;

                } catch (Exception e) {
                    Log.e("MainActivity", "Error downloading image", e);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update the ProgressBar
            progressBar.setProgress(values[0]);

            // If a new image is downloaded, update the ImageView
            if (values[0] == 0 && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Display the final image when the loop ends
            imageView.setImageBitmap(result);
        }
    }
}


