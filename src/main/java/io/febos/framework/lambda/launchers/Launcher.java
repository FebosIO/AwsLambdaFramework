/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import io.febos.framework.lambda.context.Context;
import io.febos.framework.lambda.context.ContextAWS;
import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.Intercept;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.*;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Launcher {
    public Context context;

    public static JSONObject originalRequest;
    public static String originalRequestAsString;
    public static com.google.inject.Injector injector = null;
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeHierarchyAdapter(Date.class, new JsonFormatoFechaCompleta())
            //.registerTypeHierarchyAdapter(Date.class, new JsonFormatoFechaSimple())
            //.registerTypeHierarchyAdapter(Date.class, new JsonFormatoFechaHora())
            .create();//;setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
    public static List<PreInterceptor> preInterceptors;
    public static List<PostInterceptor> postInterceptors;
    public static Response response;
    public static HashMap<String, Object> CACHE;

    public Launcher() {
        renameThread();
    }

    public void execute(InputStream inputStream, OutputStream outputStream, com.amazonaws.services.lambda.runtime.Context context) {
        try {
            String responseAsString = "{}";
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            try {
                initContext(context);
                loadOriginalRequest(inputStream);
                FunctionHolder.getInstance().context(context);
                prepareDependencyInjection();
                LambdaFunction function = injector.getInstance(LambdaFunction.class);
                loadInterceptors(function.getClass());
                FunctionHolder.getInstance().request(GSON.fromJson(originalRequestAsString, injector.getInstance(Request.class).getClass()));
                FunctionHolder.getInstance().put("requestAsJsonObject", originalRequest);
                executePreInterceptors();
                response = function.execute(FunctionHolder.getInstance().request());
                System.out.println(GSON.toJson(response));
                FunctionHolder.getInstance().response(response);
            } catch (LambdaException e) {
                e.printStackTrace();
                FunctionHolder.getInstance().response(e.getResponse());
                response = e.getResponse();
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                LambdaException ex = new LambdaException("CRITICAL_ERROR", e);
                ex.addError(e.getMessage());
                FunctionHolder.getInstance().response(ex.getResponse());
                response = ex.getResponse();
            } finally {
                executePostInterceptors();
                FunctionHolder.getInstance().response().tracingId(Thread.currentThread().getName());
                FunctionHolder.getInstance().response().time(date);
                responseAsString = GSON.toJson(FunctionHolder.getInstance().response());
            }
            try {
                outputStream.write(responseAsString.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            FunctionHolder.close();
        }
    }

    protected void loadOriginalRequest(InputStream inputStream) {
        originalRequestAsString = StringUtil.instance().inputStreamToString(inputStream);
        originalRequest = new JSONObject(originalRequestAsString);
    }

    protected void prepareDependencyInjection() {
        if (CustomInjector.getInyectors().get(originalRequest.getString("functionClass").trim()) == null) {
            try {
                CustomInjector u = new CustomInjector();
                u.configureFunction((Class<? extends LambdaFunction>) Class.forName(originalRequest.getString("functionClass")));
                u.configureRequest((Class<? extends Request>) Class.forName(originalRequest.getString("requestClass")));
                u.configureResponse((Class<? extends Response>) Class.forName(originalRequest.getString("responseClass")));
                CustomInjector.getInyectors().put(originalRequest.getString("functionClass").trim(), Guice.createInjector(u));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        injector = CustomInjector.getInyectors().get(originalRequest.getString("functionClass"));
    }

    protected void changeClasses() {
        try {
            CustomInjector u = new CustomInjector();
            u.configureFunction((Class<? extends LambdaFunction>) Class.forName(originalRequest.getString("functionClass")));
            u.configureRequest((Class<? extends Request>) Class.forName(originalRequest.getString("requestClass")));
            u.configureResponse((Class<? extends Response>) Class.forName(originalRequest.getString("responseClass")));
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
        preInterceptors.add(clazz.newInstance());
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
        try {
            Collections.reverse(postInterceptors);
            for (PostInterceptor interceptor : postInterceptors) {
                interceptor.executePostInterceptor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renameThread() {
        Thread.currentThread().setName(UUID.randomUUID().toString());
    }

    protected void initContext(com.amazonaws.services.lambda.runtime.Context context) {
        this.context = new ContextAWS(context);
    }
}
