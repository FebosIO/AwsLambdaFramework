package io.febos.framework.lambda;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

/**
 * Error handler class.
 * For beauty errors on logs and API Gateway responses
 *
 * @author Michel Munoz <michel@febos.cl>
 */
public class LambdaException extends RuntimeException {
    private final Response response;

    public LambdaException(Response response) {
        this.response =response;
    }
   
    @Override
    public String getMessage() {
        Gson builder = new GsonBuilder().setExclusionStrategies(new JsonExludeFields("stackTrace","suppressedExceptions")).create();
        return builder.toJson(this);
    }
    
    public Response getResponse(){
        return response;
    }

    public class JsonExludeFields implements ExclusionStrategy {
        public String[] fields;

        public JsonExludeFields(String... fields){
            this.fields = fields;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            if(Arrays.binarySearch(fields, f.getName())>=0)return true;
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

    }
}
