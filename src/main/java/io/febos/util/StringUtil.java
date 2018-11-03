package io.febos.util;

import java.io.*;
import java.nio.charset.Charset;

public class StringUtil {
    private static StringUtil instancia = new StringUtil();

    public static StringUtil instancia() {
        return instancia;
    }

    private StringUtil() {
    }

    public String inputStreamEnString(InputStream inputStream){
        return inputStreamEnString(inputStream,null);
    }
    public String inputStreamEnString(InputStream inputStream,Charset charset){
        StringBuilder cadena = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, charset==null?Charset.forName("UTF-8"):charset))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                cadena.append((char) c);
            }
            return cadena.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public InputStream stringEnInputStream(String cadena){
        return stringEnInputStream(cadena,null);
    }

    public InputStream stringEnInputStream(String cadena, Charset charset){
        return new ByteArrayInputStream(cadena.getBytes(charset==null?Charset.forName("UTF-8"):charset));
    }
}
