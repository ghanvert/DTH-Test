package com.example.myapplicationtest2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Pedido {

    private String text;

    public Pedido(String usuario_email, int id_producto, int cantidad, int precio_total) throws NoSuchAlgorithmException {
        this.text = usuario_email + ":" + id_producto + "x" + cantidad + "=" + precio_total;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(this.text.getBytes());

        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();

        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        this.text = sb.toString();
    }

    public String getText() {
        return this.text;
    }

}
