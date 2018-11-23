package io.febos.framework.lambda.interceptors.impl;

import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.FunctionHolder;

import java.util.Date;

public class Chronometer implements PreInterceptor, PostInterceptor {
    private static long inicio;

    @Override
    public void executePreInterceptor() {
        inicio = new Date().getTime();
    }

    @Override
    public void executePostInterceptor() {
        long duracion = new Date().getTime();
        try {
            FunctionHolder.getInstance().response().duration(duracion - inicio);
        } catch (Exception e) {
        }

    }

}
