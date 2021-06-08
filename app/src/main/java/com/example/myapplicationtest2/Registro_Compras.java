package com.example.myapplicationtest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplicationtest2.Firebase.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Registro_Compras extends AppCompatActivity {

    //private Button button;

    private String tabla = "pedidos";
    Context context;
    RecyclerView recyclerView;
    AdapterRegistroPedidos adapterRegistroPedidos;
    ArrayList<String> items;
    ArrayList<Integer> id_producto;
    ArrayList<String> price_item;
    ArrayList<String> image;
    ArrayList<Integer> aprobado;
    ArrayList<String> idPedido;

    Timer timer;

    TimerTask task;
    Connection cn;

    static int numero = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__compras);

        recyclerView = findViewById(R.id.recyclerViewPedidos);

        /*recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);*/

        /*button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro_Compras.this, Firebase.class));
            }
        });*/

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
                        timer.cancel();
                        return true;
                    case R.id.compras:
                        /*finish();
                        startActivity(getIntent());
                        overridePendingTransition(0,0);*/
                        new Tasks().execute();
                        return true;
                }
                return false;
            }
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
                handler.post(new Runnable() {
                    public void run() {
                       /* TextView text1;
                        for (int i = 0; i < 9; i++) {
                            text1 = adapterRegistroPedidos.get
                            text1.setText(String.valueOf(numero));
                        }*/
                        try {
                            @SuppressLint("StaticFieldLeak") AsyncTask myTask = new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] objects) {
                                    new Tasks().execute();
                                    numero++;
                                    return null;
                                }
                            };
                            myTask.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 6000);

    }

    public void descargarPedidos(Connection cn) throws SQLException {
        PreparedStatement pst = cn.prepareStatement("SELECT * FROM " + tabla + " WHERE usuario_email = '" + Datos_Usuario.email + "'");
        ResultSet rs = pst.executeQuery();

        /*for (int i = items.size(); i > 0; i--) {
            items.remove(i);
        }

        for (int i = image.size(); i > 0; i--) {
            image.remove(i);
        }

        for (int i = id_producto.size(); i > 0; i--) {
            id_producto.remove(i);
        }

        for (int i = aprobado.size(); i > 0; i--) {
            aprobado.remove(i);
        }

        for (int i = price_item.size(); i > 0; i--) {
            price_item.remove(i);
        }

        for (int i = idPedido.size(); i > 0; i--) {
            idPedido.remove(i);
        }*/

        items = new ArrayList<>();
        image = new ArrayList<>();
        id_producto = new ArrayList<>();
        aprobado = new ArrayList<>();
        price_item = new ArrayList<>();
        idPedido = new ArrayList<>();

        while (rs.next()) {

            /*items.removeAll(items);
            image.removeAll(image);
            id_producto.removeAll(id_producto);
            aprobado.removeAll(aprobado);
            price_item.removeAll(price_item);
            idPedido.removeAll(idPedido);

            items = new ArrayList<>();
            image = new ArrayList<>();
            id_producto = new ArrayList<>();
            aprobado = new ArrayList<>();
            price_item = new ArrayList<>();
            idPedido = new ArrayList<>();*/


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

    class Tasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                descargarPedidos(cn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //loadingDialog.startLoadingDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            recyclerView = null;
            recyclerView = findViewById(R.id.recyclerViewPedidos);
            recyclerView.setLayoutManager(new LinearLayoutManager(Registro_Compras.this));


            adapterRegistroPedidos = null;
            adapterRegistroPedidos = new AdapterRegistroPedidos(Registro_Compras.this, id_producto, items, price_item, aprobado, image, idPedido);
            recyclerView.setAdapter(adapterRegistroPedidos);

        }

    }

}