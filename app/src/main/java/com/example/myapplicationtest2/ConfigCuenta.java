package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplicationtest2.Datos.Datos_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfigCuenta extends AppCompatActivity {

    TextView nombre, telefono, direccion;
    Button cambiarDatos;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_cuenta);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.cuenta);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    startActivity(new Intent(getApplicationContext(), Ventana_principal.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.compras:
                    startActivity(new Intent(getApplicationContext(), Registro_Compras.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.cuenta:
                    return true;
            }
            return false;
        });

        nombre = findViewById(R.id._nombre);
        telefono = findViewById(R.id._telefono);
        direccion = findViewById(R.id._direccion);

        cambiarDatos = findViewById(R.id.btnCambiarDatos);
        cambiarDatos.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CambiarDatos.class)));

        nombre.setText(Datos_Usuario.nombre);
        telefono.setText(Datos_Usuario.telefono);
        direccion.setText(Datos_Usuario.direccion);


    }

}