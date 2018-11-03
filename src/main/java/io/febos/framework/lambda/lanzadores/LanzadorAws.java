/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.lanzadores;

import com.amazonaws.services.lambda.runtime.Context;
import io.febos.datos.global.SeguimientoID;
import io.febos.framework.lambda.FuncionLambda;
import io.febos.framework.lambda.Respuesta;
import io.febos.framework.lambda.Solicitud;
import io.febos.framework.lambda.contexto.ContextoAws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LanzadorAws extends Lanzador{

    @Override
    public void ejecutar(InputStream inputStream, OutputStream outputStream,Context context) {
        seguimientoId=new SeguimientoID();
        cargarSolicitudOriginal(inputStream);
        prepararInyeccionesDeDependencias();
        Lanzador.contexto=new ContextoAws(context);

        FuncionLambda funcion = inyector.getInstance(FuncionLambda.class);
        Solicitud solicitud=GSON.fromJson(solicitudOriginalComoString,inyector.getInstance(Solicitud.class).getClass());
        cargarInterceptores(funcion.getClass());
        ejecutarInterceptoresDeInicio();
        try {
            Respuesta respuesta = funcion.ejecutar(solicitud);
            Lanzador.respuesta = respuesta;
        }catch(Exception e){
            Lanzador.respuesta = new Respuesta() {
            };
        }
        ejecutarInterceptoresFinales();
        String respuestaComoString=GSON.toJson(Lanzador.respuesta);
        try {
            outputStream.write(respuestaComoString.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
