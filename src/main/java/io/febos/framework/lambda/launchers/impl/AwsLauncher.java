/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.launchers.impl;

import com.amazonaws.services.lambda.runtime.Context;
import io.febos.framework.lambda.context.ContextAWS;
import io.febos.framework.lambda.launchers.Launcher;

public class AwsLauncher extends Launcher {
    static{


    }

    @Override
    protected void initContext(Context context) {
        this.context = new ContextAWS(context);
    }

}
