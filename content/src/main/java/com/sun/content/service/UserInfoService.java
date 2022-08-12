package com.sun.content.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.content.api.dto.LoginParamDTO;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/4/11
 */
public interface UserInfoService {

 /**
  * 登录
  *
  * @param dto 用户名、密码
  * @return pms返回信息
  */
 JSONObject login(LoginParamDTO dto);
}
