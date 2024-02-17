package com.example.esporetrofit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Выполнение сетевого запроса в фоновом потоке AsyncTask
        new NetworkRequestTask().execute();
    }

    private class NetworkRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url = "https://crm.elcity.ru/api/v1/Contact?offset=0&maxSize=20";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                //метод запроса GET и заголовок X-Api-Key
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Api-Key", "35394ef793765af61ffc73725315ff2f");

                // Получение ответа
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Успешный запрос
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } else {
                    // Ошибка запроса
                    Log.e(TAG, "Request Error: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Вывод ответа в формате JSON в Logcat
            if (result != null) {
                Log.d(TAG, "Response: " + result);
            }
        }
    }
}