package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.content.SharedPreferences;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    String URL_SEND_FOR_CHECK="https://www.themealdb.com/api/json/v1/1/random.php";
    TextView responseTextLogin;
    SearchView searchV ;
    Button searchB, favBtn;
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        responseTextLogin = (TextView) findViewById(R.id.responseTextLogin);
        searchV = (SearchView)findViewById(R.id.searchView);
        searchB = (Button) findViewById(R.id.searchbtn);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    postRequest(URL_SEND_FOR_CHECK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        favBtn = (Button) findViewById(R.id.favbtn);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            postRequest("https://www.themealdb.com/api/json/v1/1/search.php?s="+searchV.getQuery());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent fav = new Intent(MainActivity.this, Favourite.class);
               MainActivity.this.startActivity(fav);
            }
        });
        responseTextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("meal", (String) responseTextLogin.getText());
                editor.commit();
            }
        });

    }



    public void postRequest(String postUrl) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        responseTextLogin.setText("Failed to Connect to Server. Please Try Again.");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseTextLogin = findViewById(R.id.responseTextLogin);
                        try {
                            String loginResponseString = response.body().string().trim();
                            Log.d("Main activity", "Response from the server : " + loginResponseString);
                            responseTextLogin.setText(loginResponseString);

                        } catch (Exception e) {
                            e.printStackTrace();
                            responseTextLogin.setText("Something went wrong. Please try again later.");
                        }
                    }
                });
            }
        });
    }
}