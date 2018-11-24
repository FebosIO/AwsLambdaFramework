package io.febos.framework.lambda.interceptors.impl.db;

public interface DatabaseConnection {
    /**
     * metod init conection
     */
    public void connect();

    /**
     * metod close conection
     */
    public void close();

    /**
     * metod called on error end function
     */
    public void onError();
}
