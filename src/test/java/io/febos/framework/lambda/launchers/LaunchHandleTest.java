package io.febos.framework.lambda.launchers;

import com.google.gson.Gson;
import io.febos.dummy.FuncionA.SolicitudA;
import io.febos.framework.lambda.shared.Response;
import org.junit.Test;

public class LaunchHandleTest {
    public static Gson GSON = new Gson();

    @Test
    public void ejecutar() {
        SolicitudA req = new SolicitudA();
        req.functionClass = "io.febos.dummy.FuncionA.FuncionA";
        req.requestClass = "io.febos.dummy.FuncionA.SolicitudA";
        req.responseClass = "io.febos.dummy.FuncionA.RespuestaA";
        req.setPrueba("asdas");

        Response respuesta = new LaunchHandler().execute(req);
        System.out.println(GSON.toJson(respuesta));
    }
}