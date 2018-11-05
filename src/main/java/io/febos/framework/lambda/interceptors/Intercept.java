package io.febos.framework.lambda.interceptors;

import java.lang.annotation.*;

@Repeatable(Interceptors.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercept {
    Class clazz();
}
