package io.febos.framework.lambda.launchers;

import com.google.gson.Gson;
import io.febos.config.CustomConector;
import io.febos.dummy.FuncionA.SolicitudA;
import io.febos.framework.lambda.interceptors.impl.db.ConnectionDb;
import io.febos.framework.lambda.shared.Response;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class LaunchHandleTest {
    public static Gson GSON = new Gson();


    @Test
    public void execute() {
        io.febos.dummy.FuncionA.SolicitudA req = new io.febos.dummy.FuncionA.SolicitudA();
        req.functionClass = "io.febos.dummy.FuncionA.FuncionA";
        req.requestClass = "io.febos.dummy.FuncionA.SolicitudA";
        req.responseClass = "io.febos.dummy.FuncionA.RespuestaA";
        req.setPrueba("asdas");
        LaunchHandler launcher=new LaunchHandler();
        Response respuesta = launcher.execute(req);
        JSONObject responseOnj=new JSONObject(launcher.responseAsString);
        System.out.println(GSON.toJson(respuesta));
    }

    @Test
    public void executeWhitNullPointException() {
        io.febos.dummy.FuncionError.SolicitudA req = new io.febos.dummy.FuncionError.SolicitudA();
        req.functionClass = "io.febos.dummy.FuncionError.FuncionA";
        req.requestClass = "io.febos.dummy.FuncionError.SolicitudA";
        req.responseClass = "io.febos.dummy.FuncionError.RespuestaA";
        req.setPrueba("asdas");
        LaunchHandler launcher=new LaunchHandler();
        Response respuesta = launcher.execute(req);
        JSONObject responseOnj=new JSONObject(launcher.responseAsString);
        TestCase.assertEquals(respuesta.message(),"CRITICAL_ERROR");
        String mensajeError = responseOnj.getJSONArray("errores").getString(0);
        TestCase.assertEquals(mensajeError,"java.lang.NullPointerException");
    }
    @Test
    public void executeInitError() {
        io.febos.dummy.FuncionError.SolicitudA req = new io.febos.dummy.FuncionError.SolicitudA();
        req.functionClass = "io.febos.dummy.FuncionErrorr.FuncionA";
        req.requestClass = "io.febos.dummy.FuncionError.SolicitudA";
        req.responseClass = "io.febos.dummy.FuncionError.RespuestaA";
        req.setPrueba("asdas");
        LaunchHandler launcher=new LaunchHandler();
        Response respuesta = launcher.execute(req);
        JSONObject responseOnj=new JSONObject(launcher.responseAsString);
        TestCase.assertEquals(respuesta.message(),"CRITICAL_ERROR");
        String mensajeError = responseOnj.getJSONArray("errores").getString(0);
        TestCase.assertEquals(mensajeError,"Error initiating Request, Not Found Class");
    }
}