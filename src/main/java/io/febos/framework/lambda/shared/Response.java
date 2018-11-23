package io.febos.framework.lambda.shared;

import java.util.ArrayList;
import java.util.List;

public abstract class Response {
    public long duration;
    public int code;
    public String tracingId;


    public Response() {
        this.tracingId = Thread.currentThread().getName();
    }

}
