package io.febos.framework.lambda.interceptors.impl.db;

import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.FunctionHolder;
import io.febos.util.ReflectionHelper;

import java.util.List;

public class ConnectionDb implements PreInterceptor, PostInterceptor {
    private static Class<? extends DbConector> conectorClass = DefaultConector.class;


    static {
        Package[] ps = Package.getPackages();
        findClassConectionLoop:
        for (Package p : ps) {
            List<Class<?>> clases = ReflectionHelper.findClassesImpmenenting(DbConector.class, p);
            if (clases != null && clases.size() > 0) {
                for (int i = 0; i < clases.size(); i++) {
                    if (!clases.get(i).getTypeName().equals(DefaultConector.class.getTypeName())) {
                        conectorClass = (Class<? extends DbConector>) clases.get(i);
                        break findClassConectionLoop;
                    }
                }
            }
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
