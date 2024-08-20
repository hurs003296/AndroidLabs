package com.example.androidlabs;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchData extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        String result = null;
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();
            connection.disconnect();
        } catch (Exception e) {
            Log.e("FetchData", "Error fetching data", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null) {
            // Handle error
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            // Process JSON object here
        } catch (Exception e) {
            Log.e("FetchData", "Error parsing JSON", e);
        }
    }
}
