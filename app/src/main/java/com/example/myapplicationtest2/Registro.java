package com.example.myapplicationtest2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registro extends AppCompatActivity {

    Button btn_registrarse;
    EditText email, password, confirmar_password, nombre_persona;
    static String _email, _password, _confirmar_password;

    public boolean connection_state;

    ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        btn_registrarse = (Button)findViewById(R.id.btn_registrarse);
        email = (EditText)findViewById(R.id.registrar_email);
        password = (EditText)findViewById(R.id.registrar_password);
        confirmar_password = (EditText)findViewById(R.id.confirmar_password);
        nombre_persona = (EditText)findViewById(R.id.nombre_persona);

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Tasks().execute();
            }
        });

    }

    public void Register(Connection cn) throws SQLException, NoSuchAlgorithmException {

        boolean register_state;

        String table = "usuarios";
        _email = email.getText().toString();
        _password = password.getText().toString();
        _confirmar_password = confirmar_password.getText().toString();
        PreparedStatement pst = cn.prepareStatement("INSERT INTO " + table + " (email, password, nombre_persona) VALUES (?, ?, ?)");
        PreparedStatement pst2 = cn.prepareStatement("SELECT email FROM " + table + " WHERE email = '" + _email + "'");
        ResultSet rs2 = pst2.executeQuery();
        if (rs2.next()) {
            register_state = false;
        } else {
            register_state = true;
        }
        rs2.close();

        if (register_state == true) {
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
                Log.e("ERROR", "Contraseñas no son iguales.");
            }
        } else {
            Log.e("ERROR", "El usuario especificado ya se encuentra registrado.");
        }
    }

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
