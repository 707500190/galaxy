package com.sun.content.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.content.api.common.constant.Constant;
import com.sun.content.api.dto.LoginParamDTO;
import com.sun.content.service.UserInfoService;
import com.sun.content.util.AESUtil;
import com.sun.content.util.MD5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/4/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    @Value("${pms-host}")
    private String PMS_HOST;

    private static final String PMS_EMPLOYEE_URL = "/pms/employee/authentication";

    @Override
    public JSONObject login(LoginParamDTO dto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONArray jsonArray = new JSONArray();
        String password = AESUtil.encryptMsgWithAES(dto.getPassword(), Constant.AES_KEY_LOGIN);
        String userName = AESUtil.encryptMsgWithAES(dto.getUserName(), Constant.AES_KEY_LOGIN);
        String verification = MD5Util.getLoginVerification(dto.getUserName(), dto.getPassword());

        headers.add("password", password);
        headers.add("loginName", userName);
        headers.add("verification", verification);
        HttpEntity<JSONArray> formEntity = new HttpEntity<>(jsonArray, headers);
        String result = restTemplate.postForObject(PMS_HOST + PMS_EMPLOYEE_URL, formEntity, String.class);
        return JSONObject.parseObject(result);
    }
}
