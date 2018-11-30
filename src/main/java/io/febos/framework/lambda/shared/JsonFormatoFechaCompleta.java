package io.febos.framework.lambda.shared;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Michel M. <michel@febos.cl>
 */
public class JsonFormatoFechaCompleta extends TypeAdapter<Date> {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final String id = UUID.randomUUID().toString();

    /**
     * @param writer
     * @param t
     * @throws IOException
     */
    @Override
    public void write(JsonWriter writer, Date t) throws IOException {
        try {
            //SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            writer.value(t.toString());
        } catch (Exception e) {
            writer.nullValue();
        }
    }

    /**
     * @param reader
     * @return
     * @throws IOException
     */
    @Override
    public Date read(JsonReader reader) throws IOException {
        //SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        if (reader.peek() != JsonToken.NULL) {
            String fecha = reader.nextString();
            try {
                return SDF.parse(fecha);
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

}