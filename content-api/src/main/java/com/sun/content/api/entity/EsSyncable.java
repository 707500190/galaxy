package com.sun.content.api.entity;

import java.util.Date;

/**
 * 同步ES数据实体公共接口
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public interface EsSyncable {

    Long getId();

    String getChannel();

}
