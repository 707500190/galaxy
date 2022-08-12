package com.sun.content.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/8/11
 */
@Configuration
@MapperScan("com.sun.content.mapper")
public class MybatisPlusConfig {


}
