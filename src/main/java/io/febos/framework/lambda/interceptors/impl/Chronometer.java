package io.febos.framework.lambda.interceptors.impl;

import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.FunctionHolder;

import java.util.Date;

public class Chronometer extends PostInterceptor implements PreInterceptor {
    private static long inicio;

    @Override
    public void executePreInterceptor() {
        inicio = new Date().getTime();
    }

    @Override
    public void onError() {
        registrarFinal();
    }

    @Override
    public void onSuccess() {
        registrarFinal();
    }

    private void registrarFinal() {
        long duracion = new Date().getTime();
        try {
            FunctionHolder.getInstance().response().duration(duracion - inicio);
        } catch (Exception e) {
        }
    }

}
