package cl.febos.lambda.dummy.FuncionB;

import io.febos.framework.lambda.Solicitud;
import javax.inject.Singleton;

@Singleton
public class SolicitudB extends Solicitud {
    private String lolo;

    public String getLolo() {
        return lolo;
    }

    public void setLolo(String lolo) {
        this.lolo = lolo;
    }
}
