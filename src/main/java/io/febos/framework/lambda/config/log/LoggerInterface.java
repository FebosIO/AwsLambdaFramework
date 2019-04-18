package io.febos.framework.lambda.config.log;

public interface LoggerInterface {

    public static void info(String message) {
        System.out.print("INFO :: ");
        System.out.println(message);
    }

    public static void info(String message, Object object) {
        System.out.print("INFO :: ");
        System.out.println(message);
        System.out.println(String.valueOf(object));
    }

    public static void debug(String message) {
        System.out.print("DEBUG :: ");
        System.out.println(message);
    }

    public static void debug(String message, Object object) {
        System.out.print("DEBUG :: ");
        System.out.println(message);
        System.out.println(String.valueOf(object));
    }

    public static void debug(String message, Throwable err) {
        System.out.print("DEBUG :: ");
        System.out.println(message);
        err.printStackTrace();
    }

    public static void error(String message) {
        System.out.print("ERROR :: ");
        System.out.println(message);
    }

    public static void error(String message, Throwable err) {
        System.out.print("ERROR :: ");
        System.out.println(message);
        err.printStackTrace();
    }
    public static void error(Throwable err) {
        System.out.print("ERROR :: ");
        err.printStackTrace();
    }

    public static void error(String message, Throwable err, Object obj) {
        System.out.print("ERROR :: ");
        System.out.println(message);
        System.out.println(String.valueOf(obj));
        err.printStackTrace();
    }
}
