/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers;

import com.amazonaws.services.lambda.runtime.Context;
import io.febos.datos.global.SeguimientoID;
import io.febos.framework.lambda.LambdaFunction;
import io.febos.framework.lambda.Response;
import io.febos.framework.lambda.Request;
import io.febos.framework.lambda.contexto.ContextoAws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AwsLauncher extends Launcher {

    @Override
    public void ejecutar(InputStream inputStream, OutputStream outputStream,Context context) {
        seguimientoId=new SeguimientoID();
        loadOriginalRequest(inputStream);
        changeClasses();
        Launcher.context =new ContextoAws(context);

        LambdaFunction funcion = injector.getInstance(LambdaFunction.class);
        Request solicitud=GSON.fromJson(originalRequestAsString, injector.getInstance(Request.class).getClass());
        loadInterceptors(funcion.getClass());
        executePreInterceptors();
        try {
            Response respuesta = funcion.execute(solicitud);
            Launcher.response = respuesta;
        }catch(Exception e){
            Launcher.response = new Response() {
            };
        }
        executePostInterceptors();
        String respuestaComoString=GSON.toJson(Launcher.response);
        try {
            outputStream.write(respuestaComoString.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
