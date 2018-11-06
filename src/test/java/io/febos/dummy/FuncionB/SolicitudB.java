package io.febos.dummy.FuncionB;

import io.febos.framework.lambda.shared.Request;
import javax.inject.Singleton;

@Singleton
public class SolicitudB extends Request {
    private String lolo;

    public String getLolo() {
        return lolo;
    }

    public void setLolo(String lolo) {
        this.lolo = lolo;
    }
}
