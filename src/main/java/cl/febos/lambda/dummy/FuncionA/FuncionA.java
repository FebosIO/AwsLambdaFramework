package cl.febos.lambda.dummy.FuncionA;

import io.febos.framework.lambda.LambdaFunction;
import io.febos.framework.lambda.interceptors.*;


@Intercept(clazz = Cronometro.class)
@Intercept(clazz = IdentificadorDeAmbiente.class)
@Intercept(clazz = IdentificadorDePais.class)
@Intercept(clazz = ConexionBaseDeDatos.class)
@Intercept(clazz = ValidarToken.class)
@Intercept(clazz = IdentificadorDeEmpresa.class)
@Intercept(clazz = IdentificadorDeGrupo.class)
@Intercept(clazz = IdentificadorDeUsuario.class)
public class FuncionA extends LambdaFunction<SolicitudA,RespuestaA> {

    public FuncionA(){
        super(SolicitudA.class,RespuestaA.class);
    }

    @Override
    public RespuestaA execute(SolicitudA request) {
        RespuestaA respuesta = new RespuestaA();
        respuesta.soloA="solo aaa!!";
        System.out.println("Funciona A!");
        return respuesta;
    }
}
