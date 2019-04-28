package io.febos.framework.lambda.shared;

public enum CoreInstanceValues implements  InstanceValuesInterface{
    REQUEST("request"),RESPONSE("response"),CONTEXT("context"),REQUEST_AS_OBJECT("requestAsJsonObject");
    String type;
    CoreInstanceValues(String type) {
        this.type=type;
    }

    @Override
    public String getType() {
        return type;
    }
}
