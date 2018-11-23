package io.febos.framework.lambda.shared;

public interface Response {
    public long duration();

    public void duration(long duration);

    public int code();

    public void code(int code);

    public String tracingId();

    public void tracingId(String tracingId);


}
