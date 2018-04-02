package com.ecity.skhg.rest.define;

import com.ecity.exception.IEnumError;

/**
 * @author
 */
public enum SKCEnumErrDef implements IEnumError{
    error_NBPortWeixin_impl_error1("error.NBPortWeixin.impl.error1","NBPortWeixin错误[0],[1]!"),
    error_NBPortWeixin_impl_error2("error.NBPortWeixin.impl.error2","NBPortWeixin错误!");
    
    
    SKCEnumErrDef(String errCode, String message){
        this.errCode= errCode;  
        this.errMessage=message;
    }
    private String errCode; 
    private String errMessage;
    
    //@Override
    public String getModelMessage() {
        return this.errMessage;
    }  
    
    //@Override
    public String getModelCode() {
        return this.errCode;
    }  
}
