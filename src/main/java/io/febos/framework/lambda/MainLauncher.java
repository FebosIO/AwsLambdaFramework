/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda;

import cl.febos.lambda.dummy.FuncionA.SolicitudA;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import io.febos.framework.lambda.launchers.Launcher;
import io.febos.framework.lambda.launchers.AwsLauncher;
import io.febos.framework.lambda.launchers.LocalLauncher;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainLauncher {
    public static Gson GSON=new Gson();
    public void ejecucionCloud(InputStream inputStream, OutputStream outputStream, Context context) {
        Launcher lanzador=new AwsLauncher();
        lanzador.ejecutar(inputStream,outputStream,context);
    }

    public Response ejecucionLocal(Request solicitud){
        Launcher lanzador=new LocalLauncher();
        InputStream inputStream = StringUtil.instancia().stringEnInputStream(GSON.toJson(solicitud));
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        String solicitudOriginalComoString = GSON.toJson(solicitud);
        JSONObject solicitudOriginal = new JSONObject(solicitudOriginalComoString);
        lanzador.ejecutar(inputStream,outputStream,null);
        try {
            return GSON.fromJson(new String(outputStream.toByteArray()),(Class<? extends Response>) Class.forName(solicitudOriginal.getString("responseClass")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }



    public static void main(String[] args) {
        SolicitudA req=new SolicitudA();
        req.functionClass ="cl.febos.lambda.dummy.FuncionA.FuncionA";
        req.requestClass ="cl.febos.lambda.dummy.FuncionA.SolicitudA";
        req.responseClass ="cl.febos.lambda.dummy.FuncionA.RespuestaA";
        req.setPrueba("asdas");

        Response respuesta = new MainLauncher().ejecucionLocal(req);
        System.out.println(GSON.toJson(respuesta));


    }


}
