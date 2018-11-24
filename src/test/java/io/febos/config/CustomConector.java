package io.febos.config;

import io.febos.framework.lambda.interceptors.impl.db.DatabaseConnection;

public class CustomConector implements DatabaseConnection {
    private static String hostDb;
    private static String user;
    private static String pass;
    private static String driver = "";


    @Override
    public void connect() {
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
