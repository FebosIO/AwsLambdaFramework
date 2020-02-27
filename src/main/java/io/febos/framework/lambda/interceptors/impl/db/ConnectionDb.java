package io.febos.framework.lambda.interceptors.impl.db;

import io.febos.framework.lambda.config.LogHolder;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;

public class ConnectionDb extends PostInterceptor implements PreInterceptor {
    public static Class<? extends DatabaseConnection> connectorClass = DefaultDatabaseConnection.class;

    private final DatabaseConnection connector;

    public DatabaseConnection getConnector() {
        return connector;
    }

    public ConnectionDb() {
        try {
            connector = connectorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LogHolder.error(e);
            throw new LambdaInitException("Error initializing db connection", e);
        }
    }

    @Override
    public void executePreInterceptor() {
        connector.connect();
    }

    @Override
    public void onError() {
        connector.onError();
    }

    @Override
    public void onSuccess() {
        connector.close();
    }
}
