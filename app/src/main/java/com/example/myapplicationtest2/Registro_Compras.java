package com.example.myapplicationtest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplicationtest2.Firebase.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Registro_Compras extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__compras);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro_Compras.this, Firebase.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.compras);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Cambio para diferentes paneles.
                switch (item.getItemId()) {
                    case R.id.inicio:
                        startActivity(new Intent(getApplicationContext(), Ventana_principal.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.compras:
                        return true;
                }
                return false;
            }
        });
    }
}