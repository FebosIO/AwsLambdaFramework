package io.febos.config;

import io.febos.framework.lambda.interceptors.impl.db.DbConector;

public class CustomConector implements DbConector {
    private static String hostDb;
    private static String user;
    private static String pass;
    private static String driver = "";


    @Override
    public void conect() {
        System.out.println("STAR CONECTION CUSTOM");
    }

    @Override
    public void close() {
        System.out.println("CLOSE CONECTION  CUSTOM");
    }

    @Override
    public void onError() {
        System.out.println("ON ERROR CONECTION  CUSTOM");
    }
}
