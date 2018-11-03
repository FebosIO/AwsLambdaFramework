/*
 * Copyright (C) Febos S.A.- Todos los derechos reservados
 * Queda expresamente prohibida la copia o reproducción total o parcial de este archivo
 * sin el permiso expreso y por escrito de Febos S.A.
 * La detección de un uso no autorizado puede acarrear el inicio de acciones legales.
 */
package io.febos.framework.lambda;

import io.febos.datos.global.FechaConHora;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Respuesta {
    public int codigo;
    public String mensaje;
    public long duracion;
    public FechaConHora fecha;
    public List<String> errores;
    public String seguimientoId;


    public void agregarError(String... error) {
        if (errores == null) {
            errores = new ArrayList<>();
        }
        this.errores.addAll(Arrays.asList(error));
    }
}
