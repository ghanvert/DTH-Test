package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigCuenta extends AppCompatActivity {

    TextView nombre, telefono, direccion;
    String db_nombre = "nombre_persona";
    String db_telefono = "telefono";
    String db_direccion = "direccion";
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
        cambiarDatos.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CambiarDatos.class));
        });

        nombre.setText(Datos_Usuario.nombre);
        telefono.setText(Datos_Usuario.telefono);
        direccion.setText(Datos_Usuario.direccion);


    }

    private void descargarDatosUsuario(Connection cn, TextView nombre, TextView telefono, TextView direccion) throws SQLException {

        PreparedStatement pst = cn.prepareStatement("SELECT * FROM usuarios WHERE email = '" + Datos_Usuario.email + "'");
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            nombre.setText(rs.getString(db_nombre));
            telefono.setText(rs.getString(db_telefono));
            direccion.setText(rs.getString(db_direccion));
        }
        rs.close();
    }

    class Tasks extends AsyncTask<Void, Void, Void> {
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                this.publishProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            try {
                TextView nombre = findViewById(R.id._nombre);
                TextView telefono = findViewById(R.id._telefono);
                TextView direccion = findViewById(R.id._direccion);
                descargarDatosUsuario(cn, nombre, telefono, direccion);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

    }
}