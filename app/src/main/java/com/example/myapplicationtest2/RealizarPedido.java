package com.example.myapplicationtest2;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class RealizarPedido extends AppCompatActivity {

    private TextView nombre_producto, precio, total;
    private EditText nombre, domicilio_entrega, telefono, cantidad;
    private ImageView image;
    private static int _precio_total = 0;

    @SuppressLint("ResourceType")
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
        Button pedir = findViewById(R.id.button_pedir);

        pedir.setOnClickListener(v -> {
            Datos_Producto.precio = precio.getText().toString();
            Datos_Producto.cantidad = Integer.parseInt(cantidad.getText().toString());
            Datos_Producto.precio_total = _precio_total;
            Datos_Producto.telefono = telefono.getText().toString();
            Datos_Producto.nombre_producto = nombre_producto.getText().toString();
            Datos_Producto.nombre_persona_recibe = nombre.getText().toString();
            Datos_Producto.domicilio_entrega = domicilio_entrega.getText().toString();

            if (Datos_Producto.nombre_persona_recibe.equalsIgnoreCase("") && !Datos_Usuario.nombre.equalsIgnoreCase("")) {
                Datos_Producto.nombre_persona_recibe = Datos_Usuario.nombre;
            }
            if (Datos_Producto.telefono.equalsIgnoreCase("") && !Datos_Usuario.telefono.equalsIgnoreCase("")) {
                Datos_Producto.telefono = Datos_Usuario.telefono;
            }
            if (Datos_Producto.domicilio_entrega.equalsIgnoreCase("") && !Datos_Usuario.direccion.equalsIgnoreCase("")) {
                Datos_Producto.domicilio_entrega = Datos_Usuario.direccion;
            }

            if (Datos_Producto.cantidad == 0 || Datos_Producto.cantidad > 99) {
                Snackbar.make(findViewById(R.layout.activity_realizar_pedido), "Ingrese una cantidad válida, porfavor.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.teal_700))
                        .show();
            } else {
                startActivity(new Intent(getApplicationContext(), Pago.class));
            }
        });

        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (cantidad.getText().toString().equals("")) {
                    total.setText("Total: 0");
                } else {
                    String str = Datos_Producto.precio;
                    str = str.replaceAll("[^\\d.]", "");
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
        setStringsInApp();
    }

    public void setStringsInApp() {
        nombre_producto.setText(Datos_Producto.nombre_producto);
        precio.setText(Datos_Producto.precio);
        image.setImageURI(Datos_Producto.image_file);
    }

}