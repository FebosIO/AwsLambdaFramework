package io.febos.framework.lambda.interceptors.impl.db;

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
        System.out.println("START CONNECTION");
    }

    @Override
    public void close() {
        System.out.println("CLOSE CONNECTION");
    }

    @Override
    public void onError() {
        System.out.println("ON ERROR CONNECTION");
    }
}
