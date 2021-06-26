package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplicationtest2.Datos.Datos_Usuario;
import com.google.android.material.snackbar.Snackbar;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Button btn_iniciar_sesion, btn_registrarse;
    EditText et_email, et_password;

    static boolean connection_state = false;

    String email_ingresado;
    String password_ingresado;

    final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_iniciar_sesion = findViewById(R.id.btn_registrarse);
        btn_registrarse = findViewById(R.id.registrarse);
        et_email = findViewById(R.id.registrar_email);
        et_password = findViewById(R.id.registrar_password);

        btn_iniciar_sesion.setOnClickListener(v -> {
            email_ingresado = et_email.getText().toString();
            password_ingresado = et_password.getText().toString();
            new Tasks().execute();
        });
        btn_registrarse.setOnClickListener(v -> {
            Intent next = new Intent(MainActivity.this, Registro.class);
            startActivity(next);
        });
    }

    protected void displayMessage(View view) {
        Snackbar.make(view, "Email y/o contraseña incorrectos", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.teal_700))
                .show();
    }

    protected void Login(Connection cn) throws NoSuchAlgorithmException {
        try {
            SHA256 sha256 = new SHA256(email_ingresado, password_ingresado);
            String hashed_password = sha256.getText();
            PreparedStatement pst = cn.prepareStatement("SELECT email FROM usuarios WHERE email = '" + email_ingresado + "' && password = '" + hashed_password + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Datos_Usuario.email = et_email.getText().toString();
                saveDatosUsuario(cn);
                Intent next = new Intent(MainActivity.this, Ventana_principal.class);
                startActivity(next);
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void test(Connection cn) throws SQLException {
        Statement statement = cn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM usuarios");
        if (rs.next()) {
            Log.d("STATE", rs.getString(1));
        }
    }

    public void saveDatosUsuario(Connection cn) throws SQLException {
        PreparedStatement pst = cn.prepareStatement("SELECT * FROM usuarios WHERE email = '" + Datos_Usuario.email + "'");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Datos_Usuario.nombre = rs.getString("nombre_persona");
            Datos_Usuario.direccion = rs.getString("direccion");
            Datos_Usuario.telefono = rs.getString("telefono");
        }
        rs.close();
    }

    @SuppressLint("StaticFieldLeak")
    class Tasks extends AsyncTask<Void, Void, Void> {
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                Login(cn);
                connection_state = true;
            } catch (Exception e) {
                e.printStackTrace();
                connection_state = false;
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            loadingDialog.startLoadingDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingDialog.dismissDialog();
            if (connection_state) {
                ConstraintLayout layout = findViewById(R.id.activity_main);
                displayMessage(layout);
            }
        }

    }

}