package io.febos.framework.lambda.launchers;

import io.febos.framework.lambda.shared.Response;
import io.febos.util.StringUtil;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LaunchHandleTest {


    @Test
    public void execute() {
        io.febos.dummy.FuncionA.SolicitudA req = new io.febos.dummy.FuncionA.SolicitudA();
        req.functionClass = "io.febos.dummy.FuncionA.FuncionA";
        req.requestClass = "io.febos.dummy.FuncionA.SolicitudA";
        req.responseClass = "io.febos.dummy.FuncionA.RespuestaA";
        req.setPrueba("asdas");
        LaunchHandler launcher=new LaunchHandler();
        Response respuesta = launcher.execute(req);
        System.out.println(LaunchHandler.lanzador.GSON.toJson(respuesta));
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


    @Test
    public void executeInitRequestError() {

        String request = "{\"prueba\":\"asdas\",\"num\":\"\",\"functionClass\":\""+io.febos.dummy.FuncionError.FuncionA.class.getName()+"\",\"requestClass\":\"a"+io.febos.dummy.FuncionError.SolicitudA.class.getName()+"\",\"responseClass\":\""+io.febos.dummy.FuncionError.RespuestaA.class.getName()+"\"}";


        InputStream reqS = StringUtil.instance().stringToInputStream(request);
        OutputStream out = new ByteArrayOutputStream();
        LaunchHandler launcher=new LaunchHandler();
        launcher.execute(reqS,out,null);

    }
}