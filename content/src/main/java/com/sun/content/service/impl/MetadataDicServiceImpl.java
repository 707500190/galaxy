package com.sun.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.content.api.entity.MetadataDic;
import com.sun.content.mapper.MetadataDicMapper;
import com.sun.content.service.MetadataDicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
@Service
@RequiredArgsConstructor
public class MetadataDicServiceImpl extends ServiceImpl<MetadataDicMapper, MetadataDic> implements MetadataDicService {


    @Override
    public Map<String, Map<String, String>> getDicMapByTypes(List<String> types) {
        QueryWrapper<MetadataDic> wrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(types)) {
            wrapper.in("type", types);
        }
        List<MetadataDic> list = this.list(wrapper);
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(MetadataDic::getType, Collectors.toMap(MetadataDic::getCode, MetadataDic::getName)));

    }

    @Override
    public Map<String, String> getDicByType(String type) {
        QueryWrapper<MetadataDic> wrapper = new QueryWrapper<>();
        wrapper.in("type", type);
        List<MetadataDic> list = this.list(wrapper);
        return list.stream().collect(Collectors.toMap(MetadataDic::getCode, MetadataDic::getName));
    }
}