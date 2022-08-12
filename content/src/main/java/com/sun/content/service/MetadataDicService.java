package com.sun.content.service;

import com.sun.content.api.entity.MetadataDic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
public interface MetadataDicService extends IService<MetadataDic> {

    /**
     * 根据type 获取字典
     *
     * @param types 类型
     * @return map<type, <code, name>>
     */
    Map<String, Map<String, String>> getDicMapByTypes(List<String> types);

    Map<String, String> getDicByType(String type);

}