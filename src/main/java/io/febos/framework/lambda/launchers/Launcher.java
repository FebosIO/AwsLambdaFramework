/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.febos.framework.lambda.config.FunctionManager;
import io.febos.framework.lambda.config.LogHolder;
import io.febos.framework.lambda.context.Context;
import io.febos.framework.lambda.context.ContextAWS;
import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.shared.FunctionHolder;
import io.febos.framework.lambda.shared.JsonFormatoFechaCompleta;
import io.febos.framework.lambda.shared.LambdaFunction;
import io.febos.framework.lambda.shared.Response;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static io.febos.framework.lambda.shared.CoreInstanceValues.REQUEST_AS_OBJECT;

public abstract class Launcher {
    public static  Gson GSON = new GsonBuilder().disableHtmlEscaping().registerTypeHierarchyAdapter(Date.class, new JsonFormatoFechaCompleta()).create();

    public Context context;
    public JSONObject originalRequest;
    public String originalRequestAsString;
    protected static FunctionManager functionManager = new FunctionManager();
    public static Response response;

    public Launcher() {
        renameThread();
    }

    public void execute(InputStream inputStream, OutputStream outputStream, com.amazonaws.services.lambda.runtime.Context context) {
        try {
            String responseAsString = "{}";
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            boolean endWhitError = false;
            try {
                initContext(context);
                loadOriginalRequest(inputStream);
                initFunctionManagerAndConfigure(originalRequest);
                loadRequestClass();
                FunctionHolder.getInstance().putValue(REQUEST_AS_OBJECT, originalRequest);
                functionManager().interceptorManager().executePreInterceptors();
                LambdaFunction function = functionManager().getLambdaInstance();
                response = function.execute(FunctionHolder.getInstance().request());
                FunctionHolder.getInstance().response(response);
                LogHolder.debug(GSON.toJson(FunctionHolder.getInstance().response()));
            } catch (LambdaException e) {
                response = functionManager().exceptionManager().processError(e);
                endWhitError = true;
            } catch (LambdaInitException e) {
                response = functionManager().exceptionManager().processError(e);
                endWhitError = true;
            } catch (Exception e) {
                response = functionManager().exceptionManager().processError(e);
                endWhitError = true;
            } finally {
                functionManager().interceptorManager().executePostInterceptors(endWhitError);
                FunctionHolder.getInstance().response().tracingId(Thread.currentThread().getName());
                FunctionHolder.getInstance().response().time(date);
                responseAsString = functionManager().responseAsString();
            }
            try {
                outputStream.write(responseAsString.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                LogHolder.error(e);
            }
        } finally {
            FunctionHolder.close();
        }
    }

    protected void loadRequestClass() {
        try {
            FunctionHolder.getInstance().request(GSON.fromJson(originalRequestAsString, functionManager().getRequestClass()));
        } catch (Exception e){
            try{
                LogHolder.error("ERROR LOAD REQUEST",originalRequestAsString);
            }catch (Exception ex){}
            throw new LambdaInitException("Error initiating Request", e);
        }
    }

    public static FunctionManager functionManager() {
        return functionManager;
    }

    public static void functionManager(FunctionManager configFunction) {
        Launcher.functionManager = configFunction;
    }

    protected void initFunctionManagerAndConfigure(JSONObject originalRequest) {
        functionManager().configure(originalRequest);
    }

    protected void loadOriginalRequest(InputStream inputStream) {
        originalRequestAsString = StringUtil.instance().inputStreamToString(inputStream);
        originalRequest = new JSONObject(originalRequestAsString);
    }


    public void renameThread() {
        Thread.currentThread().setName(UUID.randomUUID().toString());
    }

    protected void initContext(com.amazonaws.services.lambda.runtime.Context context) {
        try{
            if(context != null && context.getAwsRequestId() != null && !context.getAwsRequestId().isEmpty()){
                Thread.currentThread().setName(context.getAwsRequestId());
            }
        }catch (Exception e){}
        FunctionHolder.getInstance().context(context);
        this.context = new ContextAWS(context);
    }
}
