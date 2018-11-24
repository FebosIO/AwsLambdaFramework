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

public class LaunchHandler {
    private Launcher lanzador;

    public void execute(InputStream inputStream, OutputStream outputStream, Context context) {
        lanzador = new AwsLauncher();
        lanzador.execute(inputStream, outputStream, context);
    }

    public Response execute(Request solicitud) {
        Gson GSON = new Gson();
        lanzador = new LocalLauncher();
        InputStream inputStream = StringUtil.instance().stringToInputStream(GSON.toJson(solicitud));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String originalRequestAsString = GSON.toJson(solicitud);
        JSONObject originalRequestAsObject = new JSONObject(originalRequestAsString);
        lanzador.execute(inputStream, outputStream, null);
        try {
            String responseAsString = new String(outputStream.toByteArray());
            return GSON.fromJson(responseAsString, (Class<? extends Response>) Class.forName(originalRequestAsObject.getString("responseClass")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }

}