package io.febos.framework.lambda;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.febos.Estados;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase para manejo de errores. Encapsula en tiempo de ejecucion los distintos
 * errores registrados en la aplicacion, pudiendo ademas agregar informacion
 * adicional en la excepcion, como los argumentos ingresados que causaron el
 * error
 *
 * @author Michel Munoz <michel@febos.cl>
 */
public class FebosException extends RuntimeException {
    private final Respuesta respuesta;

    /**
     * Contructor en base a un Mensaje predefinido. Recibe como argumento un
     * mensaje predefinido como Enum
     *
     * @param estado
     */
    public FebosException(Estados estado) {
        respuesta =new Respuesta(){};
        respuesta.codigo=estado.codigo();
        respuesta.mensaje = estado.mensaje();
        respuesta.seguimientoId = Thread.currentThread().getName();
        respuesta.errores=new ArrayList<>();
    }
   
    @Override
    public String getMessage() {
        Gson builder = new GsonBuilder().setExclusionStrategies(new ExcluirCamposJson("stackTrace","suppressedExceptions")).create();
        return builder.toJson(this);
    }
    
    public FebosException addError(String error){
        this.respuesta.errores.add(error);
        return this;
    }
    /*
    public void duracion(long duracion){
        this.duracion=duracion;
    }
    public long duracion(){
        return this.duracion;
    }*/
    
    public Respuesta getRespuesta(){
        return respuesta;
    }

    public FebosException agregarError(Throwable e) {
        addSuppressed(e);
        return this;
    }

    public class ExcluirCamposJson implements ExclusionStrategy {
        public String[] campos;

        public ExcluirCamposJson(String...campos){
            this.campos=campos;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            if(Arrays.binarySearch(campos, f.getName())>=0)return true;
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

    }
}
