package cl.febos.lambda.dummy.FuncionA;

import io.febos.framework.lambda.Solicitud;
import javax.inject.Singleton;

@Singleton
public class SolicitudA extends Solicitud {
    private String prueba;

    public String getPrueba() {
        return prueba;
    }

    public void setPrueba(String prueba) {
        this.prueba = prueba;
    }
}
