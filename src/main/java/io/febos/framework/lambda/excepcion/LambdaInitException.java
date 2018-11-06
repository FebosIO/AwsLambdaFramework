package io.febos.framework.lambda.excepcion;

public class LambdaInitException extends RuntimeException {
    public LambdaInitException(String message, Exception e) {
        super(message, e);
    }
}
