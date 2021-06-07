package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class RealizarPedido extends AppCompatActivity {

    private TextView nombre_producto, precio, total;
    private EditText nombre, domicilio_entrega, telefono, cantidad;
    private ImageView image;
    private static int _precio_total = 0;
    private Button pedir, btnPagar;
    private static String _precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pedido);

        this.nombre_producto = findViewById(R.id.nombre_producto);
        this.precio = findViewById(R.id.precio);
        this.nombre = findViewById(R.id.nombre);
        this.domicilio_entrega = findViewById(R.id.domicilio_entrega);
        this.telefono = findViewById(R.id.telefono);
        this.image = findViewById(R.id.product_image_rp);
        this.cantidad = findViewById(R.id.cantidad);
        this.total = findViewById(R.id.total);
        this.pedir = findViewById(R.id.button_pedir);
        this.btnPagar = findViewById(R.id.btnPagar);

        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datos_Producto.precio = precio.getText().toString();
                Datos_Producto.cantidad = Integer.parseInt(cantidad.getText().toString());
                Datos_Producto.precio_total = _precio_total;
                Datos_Producto.telefono = telefono.getText().toString();
                Datos_Producto.nombre_producto = nombre_producto.getText().toString();
                Datos_Producto.nombre_persona_recibe = nombre.getText().toString();
                Datos_Producto.domicilio_entrega = domicilio_entrega.getText().toString();
                startActivity(new Intent(getApplicationContext(), Pago.class));
            }
        });

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (cantidad.getText().toString().equals("")) {
                    total.setText("Total: 0");
                } else {
                    String str = Datos_Producto.precio;
                    str = str.replaceAll("[^\\d.]", "");
                    _precio = str;
                    int _precio = Integer.parseInt(str);
                    int _cantidad = Integer.parseInt(cantidad.getText().toString());
                    int precio_total = _precio*_cantidad;
                    _precio_total = precio_total;
                    total.setText("Total: " + precio_total);
                    total.setGravity(Gravity.CENTER);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        getStrings();
        setStringsInApp();

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //Ejecuta tu AsyncTask!
                            AsyncTask myTask = new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] objects) {
                                    Log.e("PIXULA", "ajsodasdjkasd");
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

        timer.schedule(task, 0, 500);  //ejecutar en intervalo de 3 segundos.
    }

    public void getStrings() {
        /*Datos_Producto.nombre = "*********";
        Datos_Producto.domicilio_entrega = "******";
        Datos_Producto.telefono = "*******"*/
    }

    public void setStringsInApp() {
        nombre_producto.setText(Datos_Producto.nombre_producto);
        precio.setText(Datos_Producto.precio);
        image.setImageURI(Datos_Producto.image_file);
    }

    public void MandarPedido(Connection cn) throws SQLException, NoSuchAlgorithmException {

        int id_producto, precio_producto, cantidad, precio_total, telefono;
        String pedido, nombre_producto, usuario_email, nombre_persona_recibe, domicilio_entrega;

        usuario_email = Datos_Usuario.email;
        id_producto = Datos_Producto.id_producto;
        cantidad = Integer.parseInt(this.cantidad.getText().toString());
        precio_total = _precio_total;

        precio_producto = Integer.parseInt(_precio);
        telefono = Integer.parseInt(this.telefono.getText().toString());
        nombre_producto = this.nombre_producto.getText().toString();
        nombre_persona_recibe = this.nombre.getText().toString();
        domicilio_entrega = this.domicilio_entrega.getText().toString();

        Pedido _pedido = new Pedido(usuario_email, id_producto, cantidad, precio_total);
        pedido = _pedido.getText();

        String table = "pedidos";
        String datos = " (id_pedido, id_producto, nombre_producto, usuario_email, nombre_persona_recibe, precio_producto, cantidad, precio_total, domicilio_entrega, telefono)";
        PreparedStatement pst = cn.prepareStatement("INSERT INTO `I6U9yGtbl0`.`pedidos` (`pedido`, `id_producto`, `nombre_producto`, `usuario_email`, `nombre_persona_recibe`, `precio_producto`, `cantidad`, `precio_total`, `domicilio_entrega`, `telefono`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        pst.setString(1, pedido);
        pst.setInt(2, id_producto);
        pst.setString(3, nombre_producto);
        pst.setString(4, usuario_email);
        pst.setString(5, nombre_persona_recibe);
        pst.setInt(6, precio_producto);
        pst.setInt(7, cantidad);
        pst.setInt(8, precio_total);
        pst.setString(9, domicilio_entrega);
        pst.setInt(10, telefono);

        pst.executeUpdate();

    }

    class Tasks extends AsyncTask<Void, Void, Void> {
        protected Connection cn;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/I6U9yGtbl0?user=I6U9yGtbl0&password=1Y5MgbI0EF");
                MandarPedido(cn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(getApplicationContext(), Ventana_principal.class));
        }

    }

}