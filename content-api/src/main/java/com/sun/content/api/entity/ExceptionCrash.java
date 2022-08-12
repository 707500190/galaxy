package com.sun.content.api.entity;

import org.springframework.stereotype.Component;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/7/26
 */
@Component
public class ExceptionCrash implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("fhishfioehfoewjfowefjwoie");
    }

    public void init(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

    }
}
