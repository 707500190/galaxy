package com.sun.content.controller;


import com.sun.content.service.MetadataDicService;
import com.sun.content.api.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  元数据字典信息接口类
 * </p>
 *
 * @author sunshilong
 * @since 2022-03-14
 */
@RestController
@RequiredArgsConstructor
@Api(value = "MetadataDicController", tags = {""})
@RequestMapping("/metadataDic")

public class MetadataDicController {

private final MetadataDicService metadataDicService;
    /**
     * 获取字典集合
     *
     * @param types 字典类型
     * @return 字典集合
     */
//    @SysLog("获取字典集合")
    @ApiOperation(value = "获取字典集合")
    @GetMapping("/getByTypes")
    public R<Map<String, Map<String, String>>> page(@RequestParam("types") List<String> types) {
        return R.ok(metadataDicService.getDicMapByTypes(types));
    }

}

