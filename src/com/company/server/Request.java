package com.company.server;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    private String sessionId;
    private String method;
    private Class[] argsTypes;
    private Object[] args;

    public Request(String sessionId, String method, Class[] argsTypes, Object[] args) {
        this.sessionId = sessionId;
        this.method = method;
        this.argsTypes = argsTypes;
        this.args = args;
    }

    @Override
    public String toString() {
        return sessionId + ":" + method + ":" + Arrays.toString(args);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class[] getArgsTypes() {
        return argsTypes;
    }

    public void setArgsTypes(Class[] argsTypes) {
        this.argsTypes = argsTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
