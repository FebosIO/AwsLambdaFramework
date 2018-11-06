package io.febos.framework.lambda.shared;

import java.util.ArrayList;
import java.util.List;

public abstract class Response {
    public long duration;

    public Response() {
        this.tracingId = Thread.currentThread().getName();
    }

    public String tracingId;

}
