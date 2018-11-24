package io.febos.framework.lambda.interceptors.impl.db;

import io.febos.framework.lambda.excepcion.ErrorResponse;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.FunctionHolder;
import org.reflections.Reflections;

import java.util.Set;

public class ConnectionDb implements PreInterceptor, PostInterceptor {
    private static Class<? extends DatabaseConnection> connectorClass = DefaultDatabaseConnection.class;

    static {
        Reflections scanner = new Reflections("io.febos.config");
        Set<Class<? extends DatabaseConnection>> configClass =scanner.getSubTypesOf(DatabaseConnection.class);
        if(configClass.iterator().hasNext()){
            connectorClass =configClass.iterator().next();
        }
    }

    private final DatabaseConnection connector;

    public ConnectionDb() {
        try {
            connector = connectorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new LambdaInitException("Error initializing db connection", e);
        }
    }

    @Override
    public void executePreInterceptor() {
        connector.connect();
    }

    @Override
    public void executePostInterceptor() {
        if (FunctionHolder.getInstance().response() instanceof ErrorResponse) {
            //call error end
            connector.onError();
        }
        connector.close();
    }
}
