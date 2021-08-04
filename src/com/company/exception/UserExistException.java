package com.company.exception;

import com.company.utils.Consts;

public class UserExistException extends BaseException{
    public UserExistException(){
        super();
        code= Consts.ERR_USER_EXIST;
    }
    public UserExistException(String message){
        super(message);
        code=Consts.ERR_USER_EXIST;
    }
}
