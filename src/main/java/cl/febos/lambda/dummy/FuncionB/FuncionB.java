package cl.febos.lambda.dummy.FuncionB;

import io.febos.framework.lambda.FuncionLambda;
import javax.inject.Singleton;

@Singleton
public class FuncionB extends FuncionLambda<SolicitudB, RespuestaB> {

    public FuncionB(){
        super(SolicitudB.class,RespuestaB.class);
    }

    @Override
    public RespuestaB ejecutar(SolicitudB request) {
        System.out.println("Ejecucion Lambda B: "+request.getLolo() );
        return new RespuestaB();
    }
}
