package io.febos.dummy.FuncionA;

import io.febos.framework.lambda.interceptors.impl.Chronometer;
import io.febos.framework.lambda.interceptors.impl.db.ConnectionDb;
import io.febos.framework.lambda.shared.LambdaFunction;
import io.febos.framework.lambda.interceptors.*;

import java.util.Date;


@Intercept(clazz = Chronometer.class)
@Intercept(clazz = ConnectionDb.class)
public class FuncionA extends LambdaFunction<SolicitudA, RespuestaA> {

    public FuncionA() {
        super(SolicitudA.class, RespuestaA.class);
    }

    @Override
    public RespuestaA execute(SolicitudA request) {
        RespuestaA respuesta = new RespuestaA();
        respuesta.soloA = "solo aaa!!";
        respuesta.date = new Date();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Funciona A!");
        return respuesta;
    }
}
