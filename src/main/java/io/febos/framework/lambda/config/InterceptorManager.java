package io.febos.framework.lambda.config;

import io.febos.framework.lambda.excepcion.LambdaInitException;
import io.febos.framework.lambda.interceptors.Intercept;
import io.febos.framework.lambda.interceptors.PostInterceptor;
import io.febos.framework.lambda.interceptors.PreInterceptor;
import io.febos.framework.lambda.shared.LambdaFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterceptorManager {
    private List<PreInterceptor> preInterceptors;
    private List<PostInterceptor> postInterceptors;

    public InterceptorManager() {
        preInterceptors = new ArrayList<>();
        postInterceptors=new ArrayList<>();
    }

    public void loadInterceptors(Class<? extends LambdaFunction> lambdaClassHandler) {
        try {
            Intercept[] interceptors = lambdaClassHandler.getAnnotationsByType(Intercept.class);
            LogHolder.debug("interceptors load "+interceptors.length);
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

    public List<PreInterceptor> preInterceptors() {
        return preInterceptors;
    }

    public List<PostInterceptor> postInterceptors() {
        return postInterceptors;
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

    public void executePreInterceptors() {
        for (PreInterceptor interceptor : preInterceptors) {
            interceptor.executePreInterceptor();
        }
    }

    public void executePostInterceptors(boolean endWhitError) {
        try {
            Collections.reverse(postInterceptors);
            for (PostInterceptor interceptor : postInterceptors) {
                interceptor.executePostInterceptor(endWhitError);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
