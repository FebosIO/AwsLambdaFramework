/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda;

public abstract class FuncionLambda<T extends Solicitud,U extends Respuesta> {
    private Class T;
    private Class U;

    public FuncionLambda(Class<? extends Solicitud> T,Class<? extends Respuesta> U){
        this.T=T;
        this.U=U;
    }

    /**
     * Implementación de la funcionalidad de un lambda.
     * @param request un objeto de clase que extienda del BaseRequest
     * @return un objeto de clase que extienda del BaseResponse
     */
    public abstract U ejecutar(T request);

    //TODO: public void actualizarInformacionDeProcesoMasivoOtarea(T request);

    public Class<? extends Solicitud> obtenerClaseSolicitud(){
        return T;
    }
    public Class<? extends Respuesta> obtenerClaseRespuesta(){
        return U;
    }


}
