package cl.febos.lambda.dummy.FuncionA;

import io.febos.framework.lambda.FuncionLambda;
import io.febos.framework.lambda.interceptores.*;


@Interceptar(clase = Cronometro.class)
@Interceptar(clase = IdentificadorDeAmbiente.class)
@Interceptar(clase = IdentificadorDePais.class)
@Interceptar(clase = ConexionBaseDeDatos.class)
@Interceptar(clase = ValidarToken.class)
@Interceptar(clase = IdentificadorDeEmpresa.class)
@Interceptar(clase = IdentificadorDeGrupo.class)
@Interceptar(clase = IdentificadorDeUsuario.class)
public class FuncionA extends FuncionLambda<SolicitudA,RespuestaA> {

    public FuncionA(){
        super(SolicitudA.class,RespuestaA.class);
    }

    @Override
    public RespuestaA ejecutar(SolicitudA request) {
        RespuestaA respuesta = new RespuestaA();
        respuesta.soloA="solo aaa!!";
        System.out.println("Funciona A!");
        return respuesta;
    }
}
