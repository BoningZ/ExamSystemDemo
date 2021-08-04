package com.company.server;

import com.company.exception.BaseException;

import java.io.Serializable;

public class Response implements Serializable {
    private String sessionId;
    private Object value;
    private int state;
    private String message="Good";

    public Response(String sid,Object val){
        state=233;
        sessionId=sid;
        value=val;
    }

    public Response(String sid, BaseException e){
        state=e.getCode();
        sessionId=sid;
        message=e.getMessage();
    }

    public Response(String sid,Exception e){
        state=666;
        sessionId=sid;
        message=e.getMessage();
    }

    public boolean isSuccess(){return state==233;}

    public String toString() {
        return "\nSessionId:" + sessionId + "\nstate:"+ state + "\nvalue:" + value;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
