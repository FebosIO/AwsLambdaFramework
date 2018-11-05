package cl.febos.lambda.dummy.FuncionB;

import io.febos.framework.lambda.LambdaFunction;
import javax.inject.Singleton;

@Singleton
public class FuncionB extends LambdaFunction<SolicitudB, RespuestaB> {

    public FuncionB(){
        super(SolicitudB.class,RespuestaB.class);
    }

    @Override
    public RespuestaB execute(SolicitudB request) {
        System.out.println("Ejecucion Lambda B: "+request.getLolo() );
        return new RespuestaB();
    }
}
