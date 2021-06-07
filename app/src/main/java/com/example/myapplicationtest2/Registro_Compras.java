package com.example.myapplicationtest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplicationtest2.Firebase.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__compras);

        recyclerView = findViewById(R.id.recyclerViewPedidos);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

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
                        return true;
                    case R.id.compras:
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
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

        new Tasks().execute();

    }

    public void descargarPedidos(Connection cn) throws SQLException {
        PreparedStatement pst = cn.prepareStatement("SELECT * FROM " + tabla + " WHERE usuario_email = '" + Datos_Usuario.email + "'");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            items.add(rs.getString("nombre_producto"));
            image.add(rs.getString("imageFile"));
            id_producto.add(rs.getInt("id_producto"));
            aprobado.add(rs.getInt("aprobado"));
            price_item.add(rs.getString("precio_producto"));
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
            recyclerView = findViewById(R.id.recyclerViewPedidos);
            recyclerView.setLayoutManager(new LinearLayoutManager(Registro_Compras.this));
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(Registro_Compras.this).build());
            adapterRegistroPedidos = new AdapterRegistroPedidos(Registro_Compras.this, id_producto, items, price_item, aprobado, image);
            recyclerView.setAdapter(adapterRegistroPedidos);
            //loadingDialog.dismissDialog();
        }

    }

}