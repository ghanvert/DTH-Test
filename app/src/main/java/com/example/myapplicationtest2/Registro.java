package com.example.myapplicationtest2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplicationtest2.Datos.Datos_Usuario;
import com.google.android.material.snackbar.Snackbar;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registro extends AppCompatActivity {

    private EditText email, password, confirmar_password, nombre_persona;
    public boolean connection_state;
    private ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        Button btn_registrarse = findViewById(R.id.btn_registrarse);
        email = findViewById(R.id.registrar_email);
        password = findViewById(R.id.registrar_password);
        confirmar_password = findViewById(R.id.confirmar_password);
        nombre_persona = findViewById(R.id.nombre_persona);

        btn_registrarse.setOnClickListener(v -> new Tasks().execute());
    }

    public void Register(Connection cn) throws SQLException, NoSuchAlgorithmException {

        boolean register_state;

        String table = "usuarios";
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        String _confirmar_password = confirmar_password.getText().toString();
        PreparedStatement pst = cn.prepareStatement("INSERT INTO " + table + " (email, password, nombre_persona) VALUES (?, ?, ?)");
        PreparedStatement pst2 = cn.prepareStatement("SELECT email FROM " + table + " WHERE email = '" + _email + "'");
        ResultSet rs2 = pst2.executeQuery();
        register_state = !rs2.next();
        rs2.close();

        if (register_state) {
            if (_password.equals(_confirmar_password)) {
                SHA256 sha256 = new SHA256(_email, _password);
                pst.setString(1, _email);
                pst.setString(2, sha256.getText());
                pst.setString(3, nombre_persona.getText().toString());
                pst.executeUpdate();

                Datos_Usuario.email = _email;
                Intent next = new Intent(Registro.this, Ventana_principal.class);
                startActivity(next);
            } else {
                ConstraintLayout layout = findViewById(R.id.activity_registro);
                Snackbar.make(layout, "Las contraseñas son distintas.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.teal_700))
                        .show();
            }
        } else {
            ConstraintLayout layout = findViewById(R.id.activity_registro);
            Snackbar.make(layout, "El usuario ya existe.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.teal_700))
                    .show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class Tasks extends AsyncTask<Void, Void, Void> {
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                Register(cn);
                connection_state = true;
            } catch (Exception e) {
                e.printStackTrace();
                connection_state = false;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(Registro.this, android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Registrandose...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDialog.dismiss();
        }

    }

}
