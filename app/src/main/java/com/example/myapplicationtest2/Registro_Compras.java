package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Registro_Compras extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> items;
    private ArrayList<Integer> id_producto;
    private ArrayList<String> price_item;
    private ArrayList<String> image;
    private ArrayList<Integer> aprobado;
    private ArrayList<String> idPedido;

    private Timer timer;
    private TimerTask task;

    //private final LoadingPedidos loadingPedidos = new LoadingPedidos(Registro_Compras.this);

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__compras);

        recyclerView = findViewById(R.id.recyclerViewPedidos);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.compras);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    startActivity(new Intent(getApplicationContext(), Ventana_principal.class));
                    overridePendingTransition(0,0);
                    timer.cancel();
                    return true;
                case R.id.compras:
                    new Tasks().execute();
                    return true;
                case R.id.cuenta:
                    startActivity(new Intent(getApplicationContext(), ConfigCuenta.class));
                    overridePendingTransition(0,0);
                    timer.cancel();
                    return true;
            }
            return false;
        });

        items = new ArrayList<>();
        id_producto = new ArrayList<>();
        price_item = new ArrayList<>();
        image = new ArrayList<>();
        aprobado = new ArrayList<>();
        idPedido = new ArrayList<>();

        new Tasks().execute();

        final Handler handler = new Handler();
        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        @SuppressLint("StaticFieldLeak") AsyncTask myTask = new AsyncTask() {
                            @SuppressLint("WrongThread")
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                new Tasks().execute();
                                return null;
                            }
                        };
                        myTask.execute();
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }
        };
        timer.schedule(task, 0, 6000);
    }

    public void descargarPedidos(Connection cn) throws SQLException {
        String tabla = "pedidos";
        PreparedStatement pst = cn.prepareStatement("SELECT * FROM " + tabla + " WHERE usuario_email = '" + Datos_Usuario.email + "'");
        ResultSet rs = pst.executeQuery();

        items = new ArrayList<>();
        image = new ArrayList<>();
        id_producto = new ArrayList<>();
        aprobado = new ArrayList<>();
        price_item = new ArrayList<>();
        idPedido = new ArrayList<>();

        while (rs.next()) {
            items.add(rs.getString("nombre_producto"));
            image.add(rs.getString("imageFile"));
            id_producto.add(rs.getInt("id_producto"));
            aprobado.add(rs.getInt("aprobado"));
            Log.d("Entrando", "A AGREGAR...");
            Log.d("Agregando", String.valueOf(rs.getInt("aprobado")));
            price_item.add(rs.getString("precio_producto"));
            idPedido.add(rs.getString("pedido"));
        }
        rs.close();
        pst.close();
        cn.close();
    }

    @SuppressLint("StaticFieldLeak")
    class Tasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                descargarPedidos(cn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //loadingPedidos.startLoadingDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            recyclerView = null;
            recyclerView = findViewById(R.id.recyclerViewPedidos);
            recyclerView.setLayoutManager(new LinearLayoutManager(Registro_Compras.this));
            AdapterRegistroPedidos adapterRegistroPedidos = null;
            adapterRegistroPedidos = new AdapterRegistroPedidos(Registro_Compras.this, id_producto, items, price_item, aprobado, image, idPedido);
            recyclerView.setAdapter(adapterRegistroPedidos);
            //loadingPedidos.dismissDialog();
        }
    }
}