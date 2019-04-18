package io.febos.framework.lambda.interceptors;

public abstract class PostInterceptor {
    public void executePostInterceptor() {
        executePostInterceptor(false);
    }

    public void executePostInterceptor(boolean onError) {
        if (onError) {
            onError();
        } else {
            onSuccess();
        }
    }

    public abstract void onError();

    public abstract void onSuccess();


}
