/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.contexto;

import com.amazonaws.services.lambda.runtime.Context;

public class ContextoAws implements Contexto{
    public ContextoAws(Context context){

    }

    @Override
    public String codigoPais() {
        return null;
    }

    @Override
    public String ambiente() {
        return null;
    }

    @Override
    public String codigoPermiso() {
        return null;
    }

    @Override
    public Context aws() {
        return null;
    }
}
