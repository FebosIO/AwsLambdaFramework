package io.febos.framework.lambda.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.launchers.CustomInjector;
import io.febos.framework.lambda.shared.LambdaFunction;
import io.febos.framework.lambda.shared.Request;
import io.febos.framework.lambda.shared.Response;
import org.json.JSONObject;

public class FunctionManager {
    private Class<? extends LambdaFunction> lambdaClassHandler;
    protected ExceptionManager exceptionManager;
    protected InterceptorManager interceptorManager;
    private String identifierFunction;
    private Injector injector;
    private CustomInjector classFunctionHolder = new CustomInjector();

    public FunctionManager() {
        exceptionManagerInit();
        interceptorManagerInit();
    }


    public Injector configure(JSONObject requestObject) {
        identifierFunction = requestObject.getString("functionClass").trim();
        if (CustomInjector.getInyectors().get(identifierFunction) == null) {
            try {

                classFunctionHolder.identifierFunction(identifierFunction);
                lambdaClassHandler = repareFunctionClass(requestObject);
                classFunctionHolder.configureFunction(lambdaClassHandler);
                classFunctionHolder.configureRequest(prepareRequestClass(requestObject));
                classFunctionHolder.configureResponse(prepareResponseClass(requestObject));
                CustomInjector.getInyectors().put(identifierFunction, Guice.createInjector(classFunctionHolder));
            } catch (java.lang.ClassNotFoundException e) {
                processErrorClasNotFount(e);
            } catch (Exception e) {
                processError(e);
            }
        }
        loadInterceptors();
        injector = CustomInjector.getInyectors().get(identifierFunction);
        return injector;
    }

    protected void interceptorManagerInit() {
        interceptorManager = new InterceptorManager();
    }

    protected void exceptionManagerInit() {
        exceptionManager = new ExceptionManager();
    }

    protected void processErrorClasNotFount(ClassNotFoundException e) {
        throw new LambdaInitException("Error initiating Request, Not Found Class", e);
    }

    protected void processError(Exception e) {
        LogHolder.error("CRITICAL_ERROR", e);
        throw new LambdaInitException("", e);
    }

    protected Class<? extends LambdaFunction> repareFunctionClass(JSONObject requestObject) throws ClassNotFoundException {
        return (Class<? extends LambdaFunction>) Class.forName(requestObject.getString("functionClass"));
    }

    protected Class<? extends Request> prepareRequestClass(JSONObject requestObject) throws ClassNotFoundException {
        return (Class<? extends Request>) Class.forName(requestObject.getString("requestClass"));
    }

    protected Class<? extends Response> prepareResponseClass(JSONObject requestObject) throws ClassNotFoundException {
        return (Class<? extends Response>) Class.forName(requestObject.getString("responseClass"));
    }

    public ExceptionManager exceptionManager() {
        return exceptionManager;
    }

    public void loadInterceptors() {
        interceptorManager.loadInterceptors(lambdaClassHandler);
    }

    protected void exceptionManager(ExceptionManager exceptionManager) {
        this.exceptionManager = exceptionManager;
    }

    public InterceptorManager interceptorManager() {
        return interceptorManager;
    }

    protected void interceptorManager(InterceptorManager interceptorManager) {
        this.interceptorManager = interceptorManager;
    }

    public void executePreInterceptors() {
        interceptorManager.executePreInterceptors();
    }

    public LambdaFunction getLambdaInstance() {
        return injector.getInstance(LambdaFunction.class);
    }

    public Class<? extends Request> getRequestClass() {
        return classFunctionHolder.request();
    }

    public Class<? extends Request> getResponseClass() {
        return classFunctionHolder.request();
    }
}
