/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.lanzadores;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.febos.datos.global.SeguimientoID;
import io.febos.framework.lambda.FuncionLambda;
import io.febos.framework.lambda.Respuesta;
import io.febos.framework.lambda.Solicitud;
import io.febos.framework.lambda.contexto.Contexto;
import io.febos.framework.lambda.interceptores.Interceptar;
import io.febos.framework.lambda.interceptores.InterceptorFinal;
import io.febos.framework.lambda.interceptores.InterceptorInicial;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public abstract class Lanzador {
    protected static JSONObject solicitudOriginal;
    protected static String solicitudOriginalComoString;
    protected static Injector inyector = null;
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static Contexto contexto;
    public static List<InterceptorInicial> interceptoresIniciales;
    public static List<InterceptorFinal> interceptoresFinales;
    public static Respuesta respuesta;
    public static HashMap<String,Object> CACHE;
    public static SeguimientoID seguimientoId;

    public abstract void ejecutar(InputStream inputStream, OutputStream outputStream, Context context);

    protected void cargarSolicitudOriginal(InputStream inputStream) {
        solicitudOriginalComoString = StringUtil.instancia().inputStreamEnString(inputStream);
        solicitudOriginal = new JSONObject(solicitudOriginalComoString);
    }

    protected void prepararInyeccionesDeDependencias() {
        if (inyector == null) {
            try {
                Inyector u = new Inyector();
                u.configurarFuncion((Class<? extends FuncionLambda>) Class.forName(solicitudOriginal.getString("claseFuncion")));
                u.configurarSolicitud((Class<? extends Solicitud>) Class.forName(solicitudOriginal.getString("claseSolicitud")));
                u.configurarRespuesta((Class<? extends Respuesta>) Class.forName(solicitudOriginal.getString("claseRespuesta")));
                inyector = Guice.createInjector(u);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void cargarInterceptores(Class<? extends FuncionLambda> lambda) {
        Interceptar[] interceptores = lambda.getAnnotationsByType(Interceptar.class);
        interceptoresIniciales = new ArrayList<>();
        interceptoresFinales = new ArrayList<>();

        for (Interceptar interceptor : interceptores) {
            if(esInterceptorInicial(interceptor.clase())){
                agregarInterceptorInicial(interceptor.clase());
            }
            if(esInterceptorFinal(interceptor.clase())){
                agregarInterceptorFinal(interceptor.clase());
            }
        }

    }

    private void agregarInterceptorInicial(Class<? extends InterceptorInicial> clase){
        try {
            interceptoresIniciales.add((InterceptorInicial) clase.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarInterceptorFinal(Class<? extends InterceptorFinal> clase){
        try {
            interceptoresFinales.add((InterceptorFinal) clase.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean esInterceptorInicial(Class clase){
        try {
            return InterceptorInicial.class.isAssignableFrom(clase);
        }catch(Exception e){
            return false;
        }
    }

    private boolean esInterceptorFinal(Class clase){
        try {
            return InterceptorFinal.class.isAssignableFrom(clase);
        }catch(Exception e){
            return false;
        }
    }

    protected void ejecutarInterceptoresDeInicio() {
        for (InterceptorInicial interceptor : interceptoresIniciales) {
            interceptor.ejecutarInterceptorInicial();
        }
    }

    protected void ejecutarInterceptoresFinales() {
        for (InterceptorFinal interceptor : interceptoresFinales) {
            interceptor.ejecutarInterceptorFinal();
        }
    }

}
