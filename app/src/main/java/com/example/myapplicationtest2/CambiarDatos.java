package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CambiarDatos extends AppCompatActivity {

    private Button btnCambiarDatos;
    private EditText cambiarNombre, cambiarDireccion, cambiarTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_datos);

        btnCambiarDatos = findViewById(R.id.btnIngresarNuevosDatos);
        cambiarNombre = findViewById(R.id.cambiarNombre);
        cambiarDireccion = findViewById(R.id.cambiarDireccion);
        cambiarTelefono = findViewById(R.id.cambiarTelefono);

        btnCambiarDatos.setOnClickListener(v -> {
            new Tasks().execute();
        });

    }

    private void cambiarDatos(Connection cn) throws SQLException {

        String nuevoNombre, nuevaDireccion, nuevoTelefono;

        nuevoNombre = Datos_Usuario.nombre;
        nuevaDireccion = Datos_Usuario.direccion;
        nuevoTelefono = Datos_Usuario.telefono;

        if (cambiarNombre.getText().toString().equals("")) {
            // Mantener nombre
            nuevoNombre = Datos_Usuario.nombre;
        } else if (!(cambiarNombre.getText().toString().length() < 3)) {
            nuevoNombre = cambiarNombre.getText().toString();
        }

        if (cambiarDireccion.getText().toString().equals("")) {
            // Mantener dirección
            nuevaDireccion = Datos_Usuario.direccion;
        } else if (!(cambiarDireccion.getText().toString().length() < 6)) {
            nuevaDireccion = cambiarDireccion.getText().toString();
        }

        if (cambiarTelefono.getText().toString().equals("")) {
            // Mantener teléfono
            nuevoTelefono = Datos_Usuario.telefono;
        } else if (!(cambiarTelefono.getText().toString().length() < 9)) {
            nuevoTelefono = cambiarTelefono.getText().toString();
        }

        PreparedStatement pst = cn.prepareStatement("UPDATE usuarios SET nombre_persona = '" + nuevoNombre + "', "
                                                        + "direccion = '" + nuevaDireccion + "', telefono = '" + nuevoTelefono + "' " +
                                                          "WHERE email = '" + Datos_Usuario.email + "'");
        pst.executeUpdate();

        Datos_Usuario.nombre = nuevoNombre;
        Datos_Usuario.telefono = nuevoTelefono;
        Datos_Usuario.direccion = nuevaDireccion;

        startActivity(new Intent(getApplicationContext(), ConfigCuenta.class));
    }

    class Tasks extends AsyncTask<Void, Void, Void> {
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                cambiarDatos(cn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}