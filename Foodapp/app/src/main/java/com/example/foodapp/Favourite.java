package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import static com.example.foodapp.MainActivity.MyPREFERENCES;

public class Favourite extends AppCompatActivity {

    TextView fav ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        SharedPreferences pref = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE); // 0 - for private
        fav = (TextView)findViewById(R.id.favtxt);
        fav.setText(pref.getString("meal", "No favourite meal saved yet!"));
    }
}
