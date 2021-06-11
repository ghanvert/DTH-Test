package com.example.myapplicationtest2;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class Pago extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private static final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    private static final String API_CHECK_OUT = "http://10.0.2.2/braintree/checkout.php";

    private String token, amount;
    private HashMap<String, String> paramsHash;

    private Button pagar;
    private ProgressDialog _mDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amount = String.valueOf(Datos_Producto.precio_total);
        pagar = findViewById(R.id.pagar);

        pagar.setText(amount + " USD");

        new getToken().execute();

        pagar.setOnClickListener(v -> submitPayment());
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
                assert data != null;
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                assert nonce != null;
                String strNonce = nonce.getNonce();
                if (!amount.equalsIgnoreCase("")) {
                    amount = String.valueOf(Datos_Producto.precio_total);
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
                assert data != null;
                Exception error = (Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("EDMT_ERROR", error.toString());
            }
        }
    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(Pago.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                response -> {
                    if (response.contains("Successful")) {
                        Toast.makeText(Pago.this, "Transaction successful!", Toast.LENGTH_SHORT).show();
                        new Tasks().execute();
                    } else {
                        Toast.makeText(Pago.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("EDMT_ERROR", response);
                }, error -> Log.d("EDMT_ERROR", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void MandarPedido(Connection cn) throws SQLException, NoSuchAlgorithmException {

        int id_producto, precio_producto, cantidad, precio_total, telefono;
        String pedido, nombre_producto, usuario_email, nombre_persona_recibe, domicilio_entrega, image;

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
        image = Datos_Producto.image_file_name;

        Pedido _pedido = new Pedido(usuario_email, id_producto, cantidad, precio_total);
        pedido = _pedido.getText();

        PreparedStatement pst = cn.prepareStatement("INSERT INTO `I6U9yGtbl0`.`pedidos` (`pedido`, `id_producto`, `nombre_producto`, `usuario_email`, `nombre_persona_recibe`, `precio_producto`, `cantidad`, `precio_total`, `domicilio_entrega`, `telefono`, `imageFile`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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
        pst.setString(11, image);

        pst.executeUpdate();
    }

    @SuppressLint("StaticFieldLeak")
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
        protected void onPreExecute() {
            _mDialog = new ProgressDialog(Pago.this, android.R.style.Theme_DeviceDefault_Dialog);
            _mDialog.setCancelable(false);
            _mDialog.setMessage("Enviando su pedido");
            _mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            _mDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), Ventana_principal.class));
        }

    }

    @SuppressLint("StaticFieldLeak")
    class getToken extends AsyncTask<Void, Void, Void> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(Pago.this, android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Cargando servicio de pago");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(String responseBody) {
                    runOnUiThread(() -> token = responseBody);
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