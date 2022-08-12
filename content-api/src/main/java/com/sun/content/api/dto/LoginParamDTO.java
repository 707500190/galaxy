package com.sun.content.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/4/11
 */
@Data
public class LoginParamDTO implements Serializable {

    private static final long serialVersionUID = -1;

    private String password;
    private String userName;
}
