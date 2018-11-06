package io.febos.framework.lambda.interceptors.impl.db;

class DefaultConector implements DbConector {
    private static String hostDb;
    private static String user;
    private static String pass;
    private static String driver = "";

    static {
        hostDb = System.getenv("conector_db_host");
        user = System.getenv("conector_db_user");
        pass = System.getenv("conector_db_pass");
        driver = System.getenv("conector_db_driver");
    }

    @Override
    public void conect() {
        System.out.println("STAR CONECTION");
    }

    @Override
    public void close() {
        System.out.println("CLOSE CONECTION");
    }

    @Override
    public void onError() {
        System.out.println("ON ERROR CONECTION");
    }
}
