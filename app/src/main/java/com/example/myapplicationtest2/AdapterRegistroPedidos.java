package com.example.myapplicationtest2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRegistroPedidos extends RecyclerView.Adapter<AdapterRegistroPedidos.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<Integer> id_producto;
    private List<String> priceData;
    private List<String> imageFile;
    private List<Integer> aprobado;

    Ventana_principal ventana_principal;

    AdapterRegistroPedidos(Context context, List<Integer> id_producto, List<String> data, List<String> priceData, List<Integer> aprobado, List<String> fileResource) {;
        this.layoutInflater = LayoutInflater.from(context);
        this.id_producto = id_producto;
        this.data = data;
        this.priceData = priceData;
        this.context = context;
        this.aprobado = aprobado;
        this.imageFile = fileResource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_card_view2, viewGroup, false);
        return new ViewHolder(view);
        // Añadido un comentario
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        int idProducto = id_producto.get(i);
        String title = data.get(i);
        String description = priceData.get(i);
        String image = imageFile.get(i);
        int aprobado = this.aprobado.get(i);
        viewHolder.textTitle.setText(title);
        viewHolder.textDescription.setText(description);
        Uri path = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/drawable/" + image);

        viewHolder.imageFile.setImageURI(path);

        if (aprobado == 1) {
            viewHolder.aprobado.setText("EN CAMINO");
            viewHolder.aprobado.setTextColor(Color.GREEN);
        } else {
            viewHolder.aprobado.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView idProducto, textTitle, textDescription, aprobado;
        ImageView imageFile;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idProducto = itemView.findViewById(R.id.id_producto_pedido);
            textTitle = itemView.findViewById(R.id.product_name_pedido);
            textDescription = itemView.findViewById(R.id.product_text_price_pedido);
            imageFile = itemView.findViewById(R.id.product_image_pedido);
            aprobado = itemView.findViewById(R.id.aprobado_pedido);
            cardview = itemView.findViewById(R.id.cardView2);
        }
    }

}
