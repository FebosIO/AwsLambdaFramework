package io.febos.framework.lambda.interceptores;

import io.febos.framework.lambda.lanzadores.Lanzador;

import java.util.Date;

public class Cronometro implements InterceptorInicial,InterceptorFinal{
    private static long inicio;

    @Override
    public void ejecutarInterceptorInicial() {
        inicio=new Date().getTime();
    }

    @Override
    public void ejecutarInterceptorFinal() {
        long duracion=new Date().getTime()-inicio;
        Lanzador.respuesta.duracion=duracion;
    }

}
