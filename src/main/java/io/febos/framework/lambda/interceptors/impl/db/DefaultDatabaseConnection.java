package io.febos.framework.lambda.interceptors.impl.db;

import io.febos.framework.lambda.config.LogHolder;

class DefaultDatabaseConnection implements DatabaseConnection {
    private static String hostDb;
    private static String user;
    private static String pass;
    private static String driver = "";

    static {
        hostDb = System.getenv("db_host");
        user = System.getenv("db_user");
        pass = System.getenv("db_pass");
        driver = System.getenv("db_driver");
    }

    @Override
    public void connect() {
        LogHolder.info("START CONNECTION");
    }

    @Override
    public void close() {
        LogHolder.info("CLOSE CONNECTION");
    }

    @Override
    public void onError() {
        LogHolder.info("ON ERROR CONNECTION");
    }
}
