package com.example.myapplicationtest2.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplicationtest2.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        //Get Intent
        Intent intent = this.getIntent();

        Log.d("HOLAAAA", "y bueno: " + intent.getStringExtra("PaymentDetails"));

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("AJSDALSJD", "HELP MEEEE");
        }

    }

    private void showDetails(JSONObject response, String paymentAmount) {
        Log.e("ASDASDA", "SHOW DETAILSSSSSSS");
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText(response.getString("$"+paymentAmount));
        } catch (JSONException e) {
            Log.e("DETAILS", "SHOWING DETAILS");
        }
    }

}