package io.febos.framework.lambda.config.log;

public class LoggerDefaultImpl implements LoggerInterface {
    public static void info(String message) {
        System.out.println("INFO :: ");
        System.out.println(message);
    }

    public static void info(String message, Object object) {
        System.out.println("INFO :: ");
        System.out.println(message);
        System.out.println(String.valueOf(object));
    }

    public static void debug(String message) {
        System.out.println("DEBUG :: ");
        System.out.println(message);
    }

    public static void debug(String message, Object object) {
        System.out.println("DEBUG :: ");
        System.out.println(message);
        System.out.println(String.valueOf(object));
    }

    public static void debug(String message, Throwable err) {
        System.out.println("DEBUG :: ");
        System.out.println(message);
        err.printStackTrace();
    }

    public static void error(String message) {
        System.out.println("ERROR :: ");
        System.out.println(message);
    }

    public static void error(String message, Throwable err) {
        System.out.println("ERROR :: ");
        System.out.println(message);
        err.printStackTrace();
    }
    public static void error(Throwable err) {
        System.out.println("ERROR :: ");
        err.printStackTrace();
    }

    public static void error(String message, Throwable err, Object obj) {
        System.out.println("ERROR :: ");
        System.out.println(message);
        System.out.println(String.valueOf(obj));
        err.printStackTrace();
    }
}

