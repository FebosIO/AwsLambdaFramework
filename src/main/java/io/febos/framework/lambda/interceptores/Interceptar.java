package io.febos.framework.lambda.interceptores;

import java.lang.annotation.*;

@Repeatable(Interceptores.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interceptar {
    Class clase();
}
