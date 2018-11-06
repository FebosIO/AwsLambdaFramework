package io.febos.framework.lambda.shared;

public abstract class LambdaFunction<T extends Request,U extends Response> {
    private Class T;
    private Class U;

    public LambdaFunction(Class<? extends Request> T, Class<? extends Response> U){
        this.T=T;
        this.U=U;
    }

    public abstract U execute(T request);

    public Class<? extends Request> getRequestClass(){
        return T;
    }
    public Class<? extends Response> gerResponseClass(){
        return U;
    }


}
