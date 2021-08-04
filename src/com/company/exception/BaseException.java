package com.company.exception;

public class BaseException extends Exception{
    protected int code;
    public BaseException() {}
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
    public BaseException(String message) {
        super(message);
    }
    public BaseException(Throwable cause) {
        super(cause);
    }
    public int getCode() {
        return code;
    }
    @Override
    public String toString() {
        return getCode() + ":" + super.toString();
    }
}
