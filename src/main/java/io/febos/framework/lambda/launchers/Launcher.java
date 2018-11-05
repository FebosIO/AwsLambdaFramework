/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.febos.framework.lambda.LambdaFunction;
import io.febos.framework.lambda.Response;
import io.febos.framework.lambda.Request;
import io.febos.framework.lambda.interceptors.Intercept;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public abstract class Launcher {
    protected static JSONObject originalRequest;
    protected static String originalRequestAsString;
    protected static Injector injector = null;
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static List<PreInterceptor> preInterceptors;
    public static List<PostInterceptor> postInterceptors;
    public static Response response;
    public static HashMap<String, Object> CACHE;

    public abstract void ejecutar(InputStream inputStream, OutputStream outputStream, Context context);

    protected void loadOriginalRequest(InputStream inputStream) {
        originalRequestAsString = StringUtil.instancia().inputStreamEnString(inputStream);
        originalRequest = new JSONObject(originalRequestAsString);
    }

    protected void changeClasses() {
        try {
            Inyector u = new Inyector();
            u.configurarFuncion((Class<? extends LambdaFunction>) Class.forName(originalRequest.getString("functionClass")));
            u.configurarSolicitud((Class<? extends Request>) Class.forName(originalRequest.getString("requestClass")));
            u.configurarRespuesta((Class<? extends Response>) Class.forName(originalRequest.getString("responseClass")));
            injector = Guice.createInjector(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadInterceptors(Class<? extends LambdaFunction> lambda) throws InstantiationException, IllegalAccessException {
        Intercept[] interceptors = lambda.getAnnotationsByType(Intercept.class);
        preInterceptors = new ArrayList<>();
        postInterceptors = new ArrayList<>();

        for (Intercept interceptor : interceptors) {
            if (isPreInterceptor(interceptor.clazz())) {
                addPreInterceptor(interceptor.clazz());
            }
            if (isPostInterceptor(interceptor.clazz())) {
                addPostInterceptor(interceptor.clazz());
            }
        }
    }

    private void addPreInterceptor(Class<? extends PreInterceptor> clazz) throws IllegalAccessException, InstantiationException {
        preInterceptors.add((PreInterceptor) clazz.newInstance());
    }

    private void addPostInterceptor(Class<? extends PostInterceptor> clase) throws IllegalAccessException, InstantiationException {
            postInterceptors.add((PostInterceptor) clase.newInstance());
    }


    private boolean isPreInterceptor(Class clase) {
        try {
            return PreInterceptor.class.isAssignableFrom(clase);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPostInterceptor(Class clase) {
        try {
            return PostInterceptor.class.isAssignableFrom(clase);
        } catch (Exception e) {
            return false;
        }
    }

    protected void executePreInterceptors() {
        for (PreInterceptor interceptor : preInterceptors) {
            interceptor.executePreInterceptor();
        }
    }

    protected void executePostInterceptors() {
        Collections.reverse(postInterceptors);
        for (PostInterceptor interceptor : postInterceptors) {
            interceptor.executePostInterceptor();
        }
    }

}
