/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers;

import com.google.inject.AbstractModule;
import io.febos.framework.lambda.shared.LambdaFunction;
import io.febos.framework.lambda.shared.Response;
import io.febos.framework.lambda.shared.Request;

import java.util.concurrent.ConcurrentHashMap;

public class CustomInjector extends AbstractModule {
    private static final ConcurrentHashMap<String, com.google.inject.Injector> injectors = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, com.google.inject.Injector> getInyectors() {
        return injectors;
    }

    private String identifierFunction;
    private Class function;
    private Class request;
    private Class response;

    @Override
    protected void configure() {
        bind(LambdaFunction.class).to(function);
        bind(Request.class).to(request);
        bind(Response.class).to(response);
    }

    public void configureFunction(Class<? extends LambdaFunction> function) {
        this.function = function;
    }

    public void configureRequest(Class<? extends Request> request) {
        this.request = request;
    }

    public void configureResponse(Class<? extends Response> response) {
        this.response = response;
    }

    public void identifierFunction(String identifierFunction) {
        this.identifierFunction=identifierFunction;
    }

    public Class<? extends Request> request() {
        return request;
    }
    public Class<? extends Response> response() {
        return response;
    }
}
