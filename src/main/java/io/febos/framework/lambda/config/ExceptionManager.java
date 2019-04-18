package io.febos.framework.lambda.config;

import io.febos.framework.lambda.excepcion.LambdaException;
import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.shared.FunctionHolder;
import io.febos.framework.lambda.shared.Response;

public class ExceptionManager {
    public Response processError(LambdaException e) {
        LogHolder.error(e);
        FunctionHolder.getInstance().response(e.getResponse());
        return e.getResponse();
    }

    public Response processError(LambdaInitException e) {
        LogHolder.error(e);
        LambdaException ex = new LambdaException("CRITICAL_ERROR", e);
        ex.addError(e.getMessage());
        FunctionHolder.getInstance().response(ex.getResponse());
        return ex.getResponse();
    }

    public Response processError(Exception e) {
        LogHolder.error(e);
        LambdaException ex = new LambdaException("CRITICAL_ERROR", e);
        ex.addError(e.getMessage()!= null?e.getMessage():e.getClass().getName());
        FunctionHolder.getInstance().response(ex.getResponse());
        return ex.getResponse();
    }
}
