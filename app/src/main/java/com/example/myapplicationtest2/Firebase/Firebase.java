package com.example.myapplicationtest2.Firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplicationtest2.R;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Firebase extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(Firebase.this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integración de Firebase completa...");
        bundle.putString("message", "Otro mensaje.");
        analytics.logEvent("InitScreen", bundle);


        //Firebase
    }


}