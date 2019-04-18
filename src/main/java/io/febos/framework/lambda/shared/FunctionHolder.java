package io.febos.framework.lambda.shared;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.concurrent.ConcurrentHashMap;

public class FunctionHolder {


    private static final FunctionHolder INSTANCE = new FunctionHolder();
    private final ConcurrentHashMap<String, ConcurrentValues> threads = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> config = new ConcurrentHashMap<>();


    public static ConcurrentValues getInstance() {
        return FunctionHolder.INSTANCE.thread();
    }

    public static Object getConfig(String key) {
        return config.get(key);
    }

    public static void putConfig(String key, Object val) {
        config.put(key, val);
    }

    public static void close() {
        if (FunctionHolder.INSTANCE.threads.get(Thread.currentThread().getName()) != null) {
            FunctionHolder.INSTANCE.threads.remove(Thread.currentThread().getName());
            System.gc();
        }
    }

    private ConcurrentValues thread() {
        if (threads.get(Thread.currentThread().getName()) == null) {
            threads.put(Thread.currentThread().getName(), new ConcurrentValues());
        }
        return threads.get(Thread.currentThread().getName());
    }

    public class ConcurrentValues {
        private final ConcurrentHashMap<String, Object> values = new ConcurrentHashMap<>();

        public Request request() {
            return (Request) values.get("request");
        }
        public void request(Request request) {
            values.put("request", request);
        }


        public Response response() {
            return (Response) values.get("response");
        }

        public void response(Response response) {
            values.put("response", response);
        }

        public void context(Context contexto) {
            try {
                values.put("context", contexto);
            }catch (NullPointerException e){}
        }

        public Context context() {
            return (Context) values.get("context");
        }


        public Object get(String key) {
            return values.get(key);
        }

        public void put(String key, Object value) {
            values.put(key, value);
        }

        public void functionManager() {
        }
    }
}
