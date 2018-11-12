package io.febos.framework.lambda.excepcion;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.febos.framework.lambda.shared.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Error handler class.
 * For beauty errors on logs and API Gateway responses
 *
 * @author Michel Munoz <michel@febos.cl>
 */
public class LambdaException extends RuntimeException {
    private final ErrorResponse response;

    public LambdaException(ErrorResponse response) {
        this.response = response;
    }

    public LambdaException(String menssage, Throwable e) {
        super(menssage, e);
        this.response = new ErrorResponse();
        ((ErrorResponse) this.response).message = menssage;
    }

    @Override
    public String getMessage() {
        Gson builder = new GsonBuilder().setExclusionStrategies(new JsonExludeFields("stackTrace", "suppressedExceptions")).create();
        return builder.toJson(this);
    }

    public Response getResponse() {
        return response;
    }

    public void addError(String message) {
        this.response.errores.add(message);
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


    public class ErrorResponse extends Response {
        public String message;
        public List<String> errores = new ArrayList<>();

    }
}
