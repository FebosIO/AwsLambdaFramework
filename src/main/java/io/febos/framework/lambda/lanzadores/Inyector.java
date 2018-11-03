/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.lanzadores;

import com.google.inject.AbstractModule;
import io.febos.framework.lambda.FuncionLambda;
import io.febos.framework.lambda.Respuesta;
import io.febos.framework.lambda.Solicitud;

public class Inyector extends AbstractModule {
    private Class funcion;
    private Class solicitud;
    private Class respuesta;

    @Override
    protected void configure() {
        bind(FuncionLambda.class).to(funcion);
        bind(Solicitud.class).to(solicitud);
        bind(Respuesta.class).to(respuesta);
    }

    public void configurarFuncion(Class<? extends FuncionLambda> funcion){
        System.out.println("funcion: "+funcion.getName());
        this.funcion=funcion;
    }
    public void configurarSolicitud(Class<? extends Solicitud> solicitud){
        System.out.println("solicitud: "+funcion.getName());
        this.solicitud=solicitud;
    }
    public void configurarRespuesta(Class<? extends Respuesta> respuesta){
        System.out.println("respuesta: "+funcion.getName());
        this.respuesta=respuesta;
    }
}
