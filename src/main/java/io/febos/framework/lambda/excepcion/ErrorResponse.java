package io.febos.framework.lambda.excepcion;

import io.febos.framework.lambda.shared.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorResponse implements Response {

    public Long duration;

    public Integer code;

    public String tracingId;
    private String hora;

    @Override
    public String time() {
        return hora;
    }

    @Override
    public void time(String hora) {
        this.hora = hora;
    }

    @Override
    public long duration() {
        return duration;
    }

    @Override
    public void duration(long duration) {
        this.duration = duration;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public void message(String messaje) {
        this.message = messaje;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public void code(int code) {
        this.code = code;
    }

    @Override
    public String tracingId() {
        return tracingId;
    }

    @Override
    public void tracingId(String tracingId) {
        this.tracingId = tracingId;
    }

    public String message;
    public List<String> errores = new ArrayList<>();

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}
