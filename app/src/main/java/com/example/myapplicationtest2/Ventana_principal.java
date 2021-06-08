package com.example.myapplicationtest2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ventana_principal extends AppCompatActivity {

    // Variables de la base de datos.
    private int idProducto;
    private String nombreProducto;
    private int oferta;
    private int precio;

    // Columnas
    private final String _id = "idProducto";
    private final String _nombre = "nombreProducto";
    private final String _oferta = "oferta";
    private final String _precio = "precio";

    // Tabla
    private String tabla = "productos";

    Context context;

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<String> items;
    ArrayList<Integer> id_producto;
    ArrayList<String> price_item;
    ArrayList<Integer> oferta_item;
    ArrayList<String> image;

    final LoadingProducts loadingDialog = new LoadingProducts(Ventana_principal.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_principal);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.inicio);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Cambio para diferentes paneles.
                switch (item.getItemId()) {
                    case R.id.inicio:
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.compras:
                        startActivity(new Intent(getApplicationContext(), Registro_Compras.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        items = new ArrayList<>();
        id_producto = new ArrayList<>();
        price_item = new ArrayList<>();
        oferta_item = new ArrayList<>();
        image = new ArrayList<>();

        new Tasks().execute();

    }

    public void realizarPedido() {
        startActivity(new Intent(getApplicationContext(), RealizarPedido.class));
        //Log.e("no-error", "HOLA XD");
    }

    public void descargarProductos(Connection cn) throws SQLException {
        PreparedStatement pst = cn.prepareStatement("SELECT * FROM " + tabla + " WHERE oferta = 1");
        PreparedStatement pst2 = cn.prepareStatement("SELECT * FROM " + tabla + " WHERE oferta = 0");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            items.add(rs.getString("nombreProducto"));
            id_producto.add(rs.getInt("idproducto"));
            price_item.add(rs.getString("precio") + " USD");
            oferta_item.add(rs.getInt("oferta"));
            image.add(rs.getString("imageFile"));
        }

        ResultSet rs2 = pst2.executeQuery();
        while (rs2.next()) {
            items.add(rs2.getString("nombreProducto"));
            id_producto.add(rs2.getInt("idproducto"));
            price_item.add(rs2.getString("precio") + " USD");
            oferta_item.add(rs2.getInt("oferta"));
            image.add(rs2.getString("imageFile"));
        }
        rs.close();
        rs2.close();
    }

    class Tasks extends AsyncTask<Void, Void, Void> {
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                descargarProductos(cn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog.startLoadingDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(Ventana_principal.this));
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(Ventana_principal.this).build());
            adapter = new Adapter(Ventana_principal.this, id_producto, items, price_item, oferta_item, image);
            recyclerView.setAdapter(adapter);
            loadingDialog.dismissDialog();
        }

    }

}