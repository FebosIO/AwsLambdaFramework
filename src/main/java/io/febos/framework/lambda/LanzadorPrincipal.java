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
import io.febos.framework.lambda.lanzadores.Lanzador;
import io.febos.framework.lambda.lanzadores.LanzadorAws;
import io.febos.framework.lambda.lanzadores.LanzadorLocal;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LanzadorPrincipal {
    public static Gson GSON=new Gson();
    public void ejecucionCloud(InputStream inputStream, OutputStream outputStream, Context context) {
        Lanzador lanzador=new LanzadorAws();
        lanzador.ejecutar(inputStream,outputStream,context);
    }

    public Respuesta ejecucionLocal(Solicitud solicitud){
        Lanzador lanzador=new LanzadorLocal();
        InputStream inputStream = StringUtil.instancia().stringEnInputStream(GSON.toJson(solicitud));
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        String solicitudOriginalComoString = GSON.toJson(solicitud);
        JSONObject solicitudOriginal = new JSONObject(solicitudOriginalComoString);
        lanzador.ejecutar(inputStream,outputStream,null);
        try {
            return GSON.fromJson(new String(outputStream.toByteArray()),(Class<? extends Respuesta>) Class.forName(solicitudOriginal.getString("claseRespuesta")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }



    public static void main(String[] args) {
        SolicitudA req=new SolicitudA();
        req.claseFuncion="cl.febos.lambda.dummy.FuncionA.FuncionA";
        req.claseSolicitud="cl.febos.lambda.dummy.FuncionA.SolicitudA";
        req.claseRespuesta="cl.febos.lambda.dummy.FuncionA.RespuestaA";
        req.setPrueba("asdas");

        Respuesta respuesta = new LanzadorPrincipal().ejecucionLocal(req);
        System.out.println(GSON.toJson(respuesta));


    }


}
