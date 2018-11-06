package io.febos.dummy.FuncionA;

import io.febos.framework.lambda.interceptors.impl.Chronometer;
import io.febos.framework.lambda.shared.LambdaFunction;
import io.febos.framework.lambda.interceptors.*;


@Intercept(clazz = Chronometer.class)
public class FuncionA extends LambdaFunction<SolicitudA, RespuestaA> {

    public FuncionA() {
        super(SolicitudA.class, RespuestaA.class);
    }

    @Override
    public RespuestaA execute(SolicitudA request) {
        RespuestaA respuesta = new RespuestaA();
        respuesta.soloA = "solo aaa!!";
        System.out.println("Funciona A!");
        return respuesta;
    }
}
