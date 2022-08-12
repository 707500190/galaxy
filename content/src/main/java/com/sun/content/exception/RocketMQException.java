package com.sun.content.exception;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/8/8
 */
public class RocketMQException extends RuntimeException{

    private static final long serialVersionUID = 2525332199084634610L;

    /**
     * 错误编码
     */
    protected String errCode;

    /**
     * 错误信息
     */
    protected String errMsg;

    /**
     * 无参构造函数
     */
    public RocketMQException() {
        super();
    }

    public RocketMQException(Throwable e) {
        super(e);
    }


    public RocketMQException(String errMsg) {
        super(errMsg);
    }


    /**
     * 构造器。
     * @param errCode 代码
     * @param errMsg 消息
     */
    public RocketMQException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }
}
