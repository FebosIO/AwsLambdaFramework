package io.febos.framework.lambda.interceptors.impl.db;

public interface DbConector {
    /**
     * metod init conection
     */
    public void conect();

    /**
     * metod close conection
     */
    public void close();

    /**
     * metod called on error end function
     */
    public void onError();
}
