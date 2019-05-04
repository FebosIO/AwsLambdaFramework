package io.febos.dummy.FuncionError;

import io.febos.framework.lambda.interceptors.Intercept;
import io.febos.framework.lambda.interceptors.impl.Chronometer;
import io.febos.framework.lambda.interceptors.impl.db.ConnectionDb;
import io.febos.framework.lambda.shared.LambdaFunction;


@Intercept(clazz = Chronometer.class)
@Intercept(clazz = ConnectionDb.class)
public class FuncionA extends LambdaFunction<SolicitudA, RespuestaA> {

    public FuncionA() {
        super(SolicitudA.class, RespuestaA.class);
    }

    @Override
    public RespuestaA execute(SolicitudA request) {
        throw new NullPointerException();
    }
}
