package io.febos.framework.lambda.interceptors.impl.db;

import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.FunctionHolder;
import io.febos.util.ReflectionHelper;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;

public class ConnectionDb implements PreInterceptor, PostInterceptor {
    private static Class<? extends DbConector> conectorClass = DefaultConector.class;

    static {
        Reflections scanner = new Reflections("io.febos.config");
        Set<Class<? extends DbConector>> configClass =scanner.getSubTypesOf(DbConector.class);
        if(configClass.iterator().hasNext()){
            conectorClass=configClass.iterator().next();
        }
    }

    private final DbConector conector;

    public ConnectionDb() {
        try {
            conector = conectorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new LambdaInitException("Error al iniciar conector de la base de datos", e);
        }
    }

    @Override
    public void executePreInterceptor() {
        conector.conect();
    }

    @Override
    public void executePostInterceptor() {
        if (FunctionHolder.getInstance().response() instanceof LambdaException.ErrorResponse) {
            //call error end
            conector.onError();
        }
        conector.close();
    }
}
