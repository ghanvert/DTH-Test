package com.example.myapplicationtest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<Integer> id_producto;
    private List<String> priceData;
    private List<String> imageFile;
    private List<Integer> ofertaData;

    Ventana_principal ventana_principal;

    Adapter(Context context, List<Integer> id_producto, List<String> data, List<String> priceData, List<Integer> ofertaData, List<String> fileResource) {;
        this.layoutInflater = LayoutInflater.from(context);
        this.id_producto = id_producto;
        this.data = data;
        this.priceData = priceData;
        this.ofertaData = ofertaData;
        this.context = context;
        this.imageFile = fileResource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        int idProducto = id_producto.get(i);
        String title = data.get(i);
        String description = priceData.get(i);
        String image = imageFile.get(i);
        int oferta = ofertaData.get(i);
        viewHolder.textTitle.setText(title);
        viewHolder.textDescription.setText(description);
        Uri path = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/drawable/" + image);

        viewHolder.imageFile.setImageURI(path);

        Context _context = this.context;
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _context.startActivity(new Intent(_context, RealizarPedido.class));
                Datos_Producto.id_producto = idProducto;
                Datos_Producto.image_file = path;
                Datos_Producto.nombre_producto = title;
                Datos_Producto.precio = description;

            }
        });

        if (oferta == 0) {
            viewHolder.oferta.setVisibility(View.GONE);
        } else if (oferta == 1) {
            viewHolder.oferta.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView idProducto, textTitle, textDescription;
        ImageView oferta;
        ImageView imageFile;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idProducto = itemView.findViewById(R.id.id_producto);
            textTitle = itemView.findViewById(R.id.product_name);
            textDescription = itemView.findViewById(R.id.product_text_price);
            oferta = itemView.findViewById(R.id.oferta);
            imageFile = itemView.findViewById(R.id.product_image);
            cardview = itemView.findViewById(R.id.cardView);
        }
    }

}
