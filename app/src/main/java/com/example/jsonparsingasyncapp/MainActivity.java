package com.example.jsonparsingasyncapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadNewsTask downloadNewsTask = new DownloadNewsTask();
        try {
            downloadNewsTask.execute("https://newsapi.org/v2/everything?q=tesla&from=2023-03-08&sortBy=publishedAt&apiKey=222cd3405ab44213815b47920b0a7039").get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public class DownloadNewsTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String  result = "";
            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while(data != -1){
                    char ch = (char)data;
                    result +=ch;
                    data = inputStreamReader.read();

                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Log.i("result",s);
            //jason parsing
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String results = jsonObject.getString("results");
                String totalResults = jsonObject.getString("totalResults");

                Log.i("result status",status);
                Log.i("result total",totalResults);
                // Log.i("result results",results);

                JSONArray resultArray = new JSONArray(results);
                for(int i =0; i<resultArray.length(); i++){
                    JSONObject jsonPart = resultArray.getJSONObject(i);
                    Log.i("results title",jsonPart.getString("title"));
                    Log.i("results image",jsonPart.getString("image_url"));


                    //image in imageview
                    //textView

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}