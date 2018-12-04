package io.febos.framework.lambda.shared;

import java.util.Date;

public interface Response {
    public String time();

    public void time(String duration);

    public long duration();

    public void duration(long duration);

    public String message();

    public void message(String messaje);

    public int code();

    public void code(int code);

    public String tracingId();

    public void tracingId(String tracingId);


}
