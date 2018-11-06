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
import io.febos.framework.lambda.contexto.Contexto;
import io.febos.framework.lambda.contexto.ContextAWS;
import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.shared.FunctionHolder;
import io.febos.framework.lambda.shared.LambdaFunction;
import io.febos.framework.lambda.shared.Response;
import io.febos.framework.lambda.shared.Request;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.Intercept;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class Launcher {
    public Contexto contexto;

    public static JSONObject originalRequest;
    public static String originalRequestAsString;
    public static Injector injector = null;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static List<PreInterceptor> preInterceptors;
    public static List<PostInterceptor> postInterceptors;
    public static Response response;
    public static HashMap<String, Object> CACHE;

    public Launcher() {
        renameThread();
    }

    public void execute(InputStream inputStream, OutputStream outputStream, Context context) {
        try {
            String respuestaComoString = "{}";
            try {
                loadOriginalRequest(inputStream);
                prepararInyeccionesDeDependencias();
                initContext(context);
                LambdaFunction funcion = injector.getInstance(LambdaFunction.class);
                loadInterceptors(funcion.getClass());
                FunctionHolder.getInstance().request(GSON.fromJson(originalRequestAsString, injector.getInstance(Request.class).getClass()));
                executePreInterceptors();
                FunctionHolder.getInstance().response(funcion.execute(FunctionHolder.getInstance().request()));
            } catch (LambdaException e) {
                //ERRORES CONTROLADOS
                e.printStackTrace();
                FunctionHolder.getInstance().response(e.getResponse());
            } catch (Exception e) {
                e.printStackTrace();
                LambdaException ex = new LambdaException("CRITIC ERROR ", e);
                ex.addError(e.getMessage());
                FunctionHolder.getInstance().response(ex.getResponse());
            } finally {
                executePostInterceptors();
                respuestaComoString = GSON.toJson(FunctionHolder.getInstance().response());
            }
            try {
                outputStream.write(respuestaComoString.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            FunctionHolder.close();
        }
    }

    protected void loadOriginalRequest(InputStream inputStream) {
        originalRequestAsString = StringUtil.instancia().inputStreamEnString(inputStream);
        originalRequest = new JSONObject(originalRequestAsString);
    }

    protected void prepararInyeccionesDeDependencias() {
        if (Inyector.getInyectors().get(originalRequest.getString("functionClass").trim()) == null) {
            try {
                Inyector u = new Inyector();
                u.configurarFuncion((Class<? extends LambdaFunction>) Class.forName(originalRequest.getString("functionClass")));
                u.configurarSolicitud((Class<? extends Request>) Class.forName(originalRequest.getString("requestClass")));
                u.configurarRespuesta((Class<? extends Response>) Class.forName(originalRequest.getString("responseClass")));
                Inyector.getInyectors().put(originalRequest.getString("functionClass").trim(), Guice.createInjector(u));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        injector = Inyector.getInyectors().get(originalRequest.getString("functionClass"));
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

    protected void loadInterceptors(Class<? extends LambdaFunction> lambda) {
        try {
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
        } catch (Exception e) {
            throw new LambdaInitException("ERROR LOAD INTERCEPTORS", e);
        }
    }

    private void addPreInterceptor(Class<? extends PreInterceptor> clazz) throws IllegalAccessException, InstantiationException {
        preInterceptors.add( clazz.newInstance());
    }

    private void addPostInterceptor(Class<? extends PostInterceptor> clase) throws IllegalAccessException, InstantiationException {
        postInterceptors.add(clase.newInstance());
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

    public void renameThread() {
        Thread.currentThread().setName(UUID.randomUUID().toString());
    }

    protected void initContext(Context context) {
        contexto = new ContextAWS(context);
    }
}
