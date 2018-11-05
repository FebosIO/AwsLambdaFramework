/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda.contexto;

import com.amazonaws.services.lambda.runtime.Context;
import io.febos.framework.lambda.interceptors.IdentificadorDeAmbiente;
import io.febos.framework.lambda.interceptors.IdentificadorDePais;

public class ContextoLocal implements Contexto{

    public ContextoLocal(Context context){

    }

    @Override
    public String codigoPais() {
        return IdentificadorDePais.codigoPais;
    }

    @Override
    public String ambiente() {
        return IdentificadorDeAmbiente.nombre;
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
