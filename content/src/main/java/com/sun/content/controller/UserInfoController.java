package com.sun.content.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.content.api.dto.LoginParamDTO;
import com.sun.content.service.UserInfoService;
import com.sun.content.api.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/4/11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Api(value = "UserInfoController", tags = {"用户相关接口"})

public class UserInfoController {

    private final UserInfoService userInfoService;

    private static final String FLAG_SUCCESS = "1";

    /**
     * 用户登录
     *
     * @param param 登录参数
     * @return 状态码
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@RequestBody @Validated LoginParamDTO param) {
        JSONObject res = userInfoService.login(param);
        String code = res.getString("resultCode");
        String msg = res.getString("msg");
        if (FLAG_SUCCESS.equals(code)) {
            return R.ok(msg);
        } else {
            return R.failed(msg);
        }
    }

}
