package io.febos.framework.lambda.excepcion;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.febos.framework.lambda.shared.Response;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;

/**
 * Error handler class.
 * For beauty errors on logs and API Gateway responses
 *
 * @author Michel Munoz <michel@febos.cl>
 */
public class LambdaException extends RuntimeException {
    private static Class<? extends ErrorResponse> errorClass = ErrorResponse.class;

    static {
        Reflections scanner = new Reflections("io.febos.config");
        Set<Class<? extends ErrorResponse>> configClass = scanner.getSubTypesOf(ErrorResponse.class);
        if (configClass.iterator().hasNext()) {
            errorClass = configClass.iterator().next();
        }
    }

    protected ErrorResponse response;

    protected LambdaException() {
        response = getErrorInstance("ERROR");
    }

    public LambdaException(ErrorResponse response) {
        this.response = response;
    }

    public LambdaException(String menssage, Throwable e) {
        super(menssage, e);
        this.response = getErrorInstance(menssage);
        ((ErrorResponse) this.response).message = menssage;
    }

    public LambdaException(String menssage, int code, Throwable e) {
        super(menssage, e);
        this.response = getErrorInstance(menssage);
        this.response.code(code);
        ((ErrorResponse) this.response).message(menssage);
    }


    @Override
    public String getMessage() {
        Gson builder = new GsonBuilder().setExclusionStrategies(new JsonExludeFields("stackTrace", "suppressedExceptions")).create();
        return builder.toJson(this);
    }

    public Response getResponse() {
        return response;
    }

    public LambdaException addError(String message) {
        this.response.errores.add(message);
        return this;
    }

    public class JsonExludeFields implements ExclusionStrategy {
        public String[] fields;

        public JsonExludeFields(String... fields) {
            this.fields = fields;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            if (Arrays.binarySearch(fields, f.getName()) >= 0) return true;
            return false;
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

    }

    public static ErrorResponse getErrorInstance(String message) {
        ErrorResponse response;
        try {
            response = errorClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            response = new ErrorResponse(message);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response = new ErrorResponse(message);
        }
        response.message(message);
        return response;
    }

    public static LambdaException getErrorResponse(String mensaje, Exception e) {
        return null;
    }

}
