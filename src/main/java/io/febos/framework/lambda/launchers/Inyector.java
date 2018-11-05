/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers;

import com.google.inject.AbstractModule;
import io.febos.framework.lambda.LambdaFunction;
import io.febos.framework.lambda.Response;
import io.febos.framework.lambda.Request;

public class Inyector extends AbstractModule {
    private Class funcion;
    private Class solicitud;
    private Class respuesta;

    @Override
    protected void configure() {
        bind(LambdaFunction.class).to(funcion);
        bind(Request.class).to(solicitud);
        bind(Response.class).to(respuesta);
    }

    public void configurarFuncion(Class<? extends LambdaFunction> funcion){
        System.out.println("funcion: "+funcion.getName());
        this.funcion=funcion;
    }
    public void configurarSolicitud(Class<? extends Request> solicitud){
        System.out.println("solicitud: "+funcion.getName());
        this.solicitud=solicitud;
    }
    public void configurarRespuesta(Class<? extends Response> respuesta){
        System.out.println("response: "+funcion.getName());
        this.respuesta=respuesta;
    }
}
