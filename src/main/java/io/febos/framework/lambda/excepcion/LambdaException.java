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
    protected ErrorResponse response;

    protected LambdaException() {
        response = new ErrorResponse();
    }

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


    public class ErrorResponse implements Response {

        public long duration;

        public int code;

        public String tracingId;

        @Override
        public long duration() {
            return duration;
        }

        @Override
        public void duration(long duration) {
            this.duration = duration;
        }

        @Override
        public int code() {
            return code;
        }

        @Override
        public void code(int code) {
            this.code = code;
        }

        @Override
        public String tracingId() {
            return tracingId;
        }

        @Override
        public void tracingId(String tracingId) {
            this.tracingId = tracingId;
        }

        public String message;
        public List<String> errores = new ArrayList<>();

        public ErrorResponse() {
        }

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
