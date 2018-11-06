package io.febos.framework.lambda.launchers;

import com.amazonaws.services.lambda.runtime.Context;

import com.google.gson.Gson;
import io.febos.framework.lambda.launchers.impl.AwsLauncher;
import io.febos.framework.lambda.launchers.impl.LocalLauncher;
import io.febos.framework.lambda.shared.Request;
import io.febos.framework.lambda.shared.Response;
import io.febos.util.StringUtil;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LaunchHandle {
    private Launcher lanzador;

    public void ejecutar(InputStream inputStream, OutputStream outputStream, Context context) {
        lanzador = new AwsLauncher();
        lanzador.execute(inputStream, outputStream, context);
    }

    public Response ejecutar(Request solicitud) {
        Gson GSON = new Gson();
        lanzador = new LocalLauncher();
        InputStream inputStream = StringUtil.instancia().stringEnInputStream(GSON.toJson(solicitud));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String solicitudOriginalComoString = GSON.toJson(solicitud);
        JSONObject solicitudOriginal = new JSONObject(solicitudOriginalComoString);
        lanzador.execute(inputStream, outputStream, null);
        try {
            return GSON.fromJson(new String(outputStream.toByteArray()), (Class<? extends Response>) Class.forName(solicitudOriginal.getString("responseClass")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }

}