package io.febos.dummy.FuncionError;

import io.febos.framework.lambda.shared.Request;

public class SolicitudA extends Request {
    private String prueba;
    private int num;

    public String getPrueba() {
        return prueba;
    }

    public void setPrueba(String prueba) {
        this.prueba = prueba;
    }
}
