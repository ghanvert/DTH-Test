package com.example.myapplicationtest2.Pedidos;

import java.util.Map;

public class Data {

    private Map<String, Map<String, String>> map;

    public Data() {}

    public Data(Map<String, Map<String, String>> map) {
        this.map = map;
    }

    public Map<String, Map<String, String>> getMap() {
        return map;
    }

    public void setMap(Map<String, Map<String, String>> map) {
        this.map = map;
    }

}
