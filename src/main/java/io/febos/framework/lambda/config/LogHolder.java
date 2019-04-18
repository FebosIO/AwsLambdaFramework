package io.febos.framework.lambda.config;

import io.febos.framework.lambda.config.log.LoggerDefaultImpl;
import io.febos.framework.lambda.config.log.LoggerInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LogHolder {
    public static Class<? extends LoggerInterface> loggerClass = LoggerDefaultImpl.class;

    public static void info(String message, Object object) {
        callStaticMetod("info",message, object);

    }

    public static void info(String message) {
        callStaticMetod("info",message);
    }

    public static void info(Object object) {
        callStaticMetod("info","", object);
    }

    public static void debug(String message) {callStaticMetod("debug",message);

    }

    public static void debug(Object object) {
        callStaticMetod("debug","", object);
    }

    public static void debug(String message, Object object) {
        callStaticMetod("debug",message, object);
    }

    public static void error(String mensaje) {
        callStaticMetod("error",mensaje);
    }

    public static void error(Throwable err) {
        callStaticMetod("error", err);
    }

    public static void error(Object obj) {
        callStaticMetod("error","", null, obj);
    }

    public static void error(String mensaje, Object obj) {
        callStaticMetod("error",mensaje, null, obj);
    }

    public static void error(String mensaje, Throwable err) {
        callStaticMetod("error",mensaje, err, null);
    }

    public static void error(Throwable err, Object obj) {
        callStaticMetod("error","", err, obj);
    }


    public static void error(String mensaje, Throwable err, Object object) {
        callStaticMetod("error","", err, object);
    }
    private static void callStaticMetod(String metod, String params) {
        try {
            Method method = loggerClass.getMethod(metod, String.class);
            method.invoke(null, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static void callStaticMetod(String metod, String s,  Object object) {
        try {
            Method method = loggerClass.getMethod(metod, String.class,  Object.class);
            method.setAccessible(true);
            method.invoke(null, s,object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static void callStaticMetod(String metod, String s, Throwable err, Object object) {
        try {
            Method method = loggerClass.getMethod(metod, String.class, Throwable.class, Object.class);
            method.setAccessible(true);
            method.invoke(null, s,err,object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static void callStaticMetod(String metod, Throwable err) {
        try {
            Method method = loggerClass.getMethod(metod, Throwable.class);
            method.setAccessible(true);
            method.invoke(null, err);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
