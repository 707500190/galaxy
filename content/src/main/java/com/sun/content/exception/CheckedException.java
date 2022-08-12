package com.sun.content.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lengleng
 * @date 😴2018年06月22日16:21:57
 */
@Data
@NoArgsConstructor
public class CheckedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

//    private ExceptionCodeEnum exceptionCodeEnum;
//
//    public CheckedException(ExceptionCodeEnum exceptionCodeEnum) {
//        super(exceptionCodeEnum.getDesc());
//        this.code = exceptionCodeEnum.getValue();
//        this.msg = exceptionCodeEnum.getDesc();
//        this.exceptionCodeEnum = exceptionCodeEnum;
//    }


    public CheckedException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

//    public CheckedException(ExceptionCodeEnum unknownError, String errorMsg) {
//        super(errorMsg);
//        this.code = exceptionCodeEnum.getValue();
//        this.msg = errorMsg;
//        this.exceptionCodeEnum = unknownError;
//    }
}
