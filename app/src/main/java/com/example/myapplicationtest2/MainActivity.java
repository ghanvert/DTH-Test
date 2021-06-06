package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    static Connection _connection = null;

    String email_ingresado;
    String password_ingresado;

    final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_iniciar_sesion = (Button)findViewById(R.id.btn_registrarse);
        btn_registrarse = (Button)findViewById(R.id.registrarse);
        et_email = (EditText)findViewById(R.id.registrar_email);
        et_password = (EditText)findViewById(R.id.registrar_password);

        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_ingresado = et_email.getText().toString();
                password_ingresado = et_password.getText().toString();
                new Tasks().execute();
            }
        });
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this, Registro.class);
                startActivity(next);
            }
        });
    }

    protected void displayMessage(View view, String msg) {
        Snackbar.make(view, "Email y/o contraseña incorrectos", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.teal_700))
                .setAction("Acción", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Do something special
                        Log.i("Snackbar", "Snackbar pulsado.");
                    }
                })
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
                Intent next = new Intent(MainActivity.this, Ventana_principal.class);
                startActivity(next);
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //SHA256 sha256 = new SHA256(password_ingresado);
        //Log.e("PASSWORD", sha256.getText());
    }

    public void test(Connection cn) throws SQLException {
        Statement statement = cn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM usuarios");
        if (rs.next()) {
            Log.d("STATE", rs.getString(1));
        }
    }

    class Tasks extends AsyncTask<Void, Void, Void> {
        String error = "";
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                //test(cn);
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
            if (connection_state == true) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activity_main);
                displayMessage(layout,"Email y/o contraseña incorrectos.");
            }
        }

    }

}