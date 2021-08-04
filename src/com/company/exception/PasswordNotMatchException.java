package com.company.exception;

import com.company.utils.Consts;

public class PasswordNotMatchException extends BaseException{
    public PasswordNotMatchException(){
        super();
        code= Consts.ERR_PASSWORD_NOT_MATCH;
    }
    public PasswordNotMatchException(String message){
        super(message);
        code=Consts.ERR_PASSWORD_NOT_MATCH;
    }
}
