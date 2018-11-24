package io.febos.util;

import java.io.*;
import java.nio.charset.Charset;

public class StringUtil {
    private static StringUtil instance = new StringUtil();

    public static StringUtil instance() {
        return instance;
    }

    private StringUtil() {
    }

    public String inputStreamToString(InputStream inputStream){
        return inputStreamToString(inputStream,null);
    }
    public String inputStreamToString(InputStream inputStream, Charset charset){
        StringBuilder str = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, charset==null?Charset.forName("UTF-8"):charset))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                str.append((char) c);
            }
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public InputStream stringToInputStream(String input){
        return stringToInputStream(input,null);
    }

    public InputStream stringToInputStream(String text, Charset charset){
        return new ByteArrayInputStream(text.getBytes(charset==null?Charset.forName("UTF-8"):charset));
    }
}
