package com.example.myapplicationtest2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Pago extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    final String API_CHECK_OUT = "http://10.0.2.2/braintree/checkout.php";

    String token, amount;
    HashMap<String, String> paramsHash;

    Button btn_pay;
    EditText edt_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btn_pay = findViewById(R.id.payment);
        edt_amount = findViewById(R.id.amount);
        
        new getToken().execute();

        // Event
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();
                if (!edt_amount.getText().toString().isEmpty()) {
                    amount = edt_amount.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);
                    sendPayments();
                } else {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "User Cancel", Toast.LENGTH_SHORT).show();
            } else {
                Exception error = (Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("EDMT_ERROR", error.toString());
            }
        }
    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(Pago.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().contains("Successful")) {
                            Toast.makeText(Pago.this, "Transaction successful!", Toast.LENGTH_SHORT).show();
                            new Tasks().execute();
                        } else {
                            Toast.makeText(Pago.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("EDMT_ERROR", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("EDMT_ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null) {
                    return null;
                }
                Map<String, String> params = new HashMap<>();
                for (String key: paramsHash.keySet()) {
                    params.put(key, paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);

    }

    public void MandarPedido(Connection cn) throws SQLException, NoSuchAlgorithmException {

        int id_producto, precio_producto, cantidad, precio_total, telefono;
        String pedido, nombre_producto, usuario_email, nombre_persona_recibe, domicilio_entrega;

        usuario_email = Datos_Usuario.email;
        id_producto = Datos_Producto.id_producto;
        cantidad = Datos_Producto.cantidad;
        precio_total = Datos_Producto.precio_total;

        String str = Datos_Producto.precio;
        str = str.replaceAll("[^\\d.]", "");

        precio_producto = Integer.parseInt(str);
        telefono = Integer.parseInt(Datos_Producto.telefono);
        nombre_producto = Datos_Producto.nombre_producto;
        nombre_persona_recibe = Datos_Producto.nombre_persona_recibe;
        domicilio_entrega = Datos_Producto.domicilio_entrega;

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

    class getToken extends AsyncTask<Void, Void, Void> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(Pago.this, android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Please wait");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(String responseBody) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("EDMT_ERROR", exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDialog.dismiss();
        }
    }
}