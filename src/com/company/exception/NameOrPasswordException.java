package com.company.exception;

import com.company.utils.Consts;

public class NameOrPasswordException extends BaseException {
    public NameOrPasswordException() {
        super();
        code = Consts.ERR_NAME_OR_PASSWORD;
    }
    public NameOrPasswordException(String message) {
        super(message);
        code = Consts.ERR_NAME_OR_PASSWORD;
    }
}
