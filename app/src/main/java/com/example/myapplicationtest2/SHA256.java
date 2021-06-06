package com.example.myapplicationtest2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    private String text;

    public SHA256(String email, String password) throws NoSuchAlgorithmException {
        this.text = email + "@" + password;
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
