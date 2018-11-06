package io.febos.framework.lambda.interceptors.impl.db;

import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.FunctionHolder;
import io.febos.framework.lambda.shared.LambdaFunction;

import java.util.Date;

public class ConnectionDb implements PreInterceptor, PostInterceptor {
    private static Class<? extends DbConector> conectorClass = DefaultConector.class;

    static {
        String classConector = System.getenv("class_conector_db");
        if (classConector != null && !classConector.isEmpty()) {
            try {
                System.out.println("class conector found :"+classConector);
                conectorClass = (Class<? extends DbConector>) Class.forName(classConector);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private final DbConector conector;

    public ConnectionDb() {
        try {
            System.out.println("INIT CLASS CONECTION");
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
