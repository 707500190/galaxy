package com.sun.content.service.impl;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sun.content.api.dto.ReptileContentQueryDTO;
import com.sun.content.api.entity.RedContent;
import com.sun.content.api.entity.WeixinOfficialAccount;
import com.sun.content.api.vo.ReptileContentVO;
import com.sun.content.api.vo.ReptilePageVO;
import com.sun.content.mapper.RedContentMapper;
import com.sun.content.service.RedContentService;
import com.sun.content.service.WeixinOfficialAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sun.content.api.common.constant.Constant.DYNAMIC_DB_CRAWLER;


/**
 * @描述：小红书主体 服务类
 * @作者: tw
 * @日期: 2022-02-23
 */
@Service
@RequiredArgsConstructor
////@DS(DYNAMIC_DB_CRAWLER)
public class RedContentServiceImpl extends ServiceImpl<RedContentMapper, RedContent> implements RedContentService {
    private final RedContentMapper redContentMapper;

    private final WeixinOfficialAccountService weixinOfficialAccountService;

    @Override
    public IPage<ReptilePageVO> page(Page page, ReptileContentQueryDTO reptileContentQueryDTO) {
        Map<String, Boolean> sortMap = reptileContentQueryDTO.getSort();
        if (!CollectionUtils.isEmpty(sortMap)) {
            page.setOrders(constructOrder(sortMap));
        } else {
            List<OrderItem> list = Lists.newArrayList();
            list.add(new OrderItem("articleLikeNumber", false));
            page.setOrders(list);
        }
        if (Objects.nonNull(reptileContentQueryDTO)) {
            if (reptileContentQueryDTO.getChannel() == null){
                return redContentMapper.allPage(page, reptileContentQueryDTO);
            }
            if (reptileContentQueryDTO.getChannel().equals(1)) {
                return redContentMapper.page1(page, reptileContentQueryDTO);
            }
            else if (reptileContentQueryDTO.getChannel().equals(2)){
                return redContentMapper.page2(page, reptileContentQueryDTO);
            }
        }
        return redContentMapper.allPage(page, reptileContentQueryDTO);
    }

    public List<OrderItem> constructOrder(Map<String, Boolean> sortMap){
        List<OrderItem> orderItems = Lists.newArrayList();
        for (String key: sortMap.keySet()){
            orderItems.add(new OrderItem(key, sortMap.get(key)));
        }
        return orderItems;
    }

    @Override
    public ReptileContentVO getDetailById(Integer channel, Long id) {
        ReptileContentVO reptileContentVO = new ReptileContentVO();
        //channel为1查询小红书
        if (channel == 1) {
            RedContent redContent = this.getById(id);
            BeanUtils.copyProperties(redContent, reptileContentVO);
            reptileContentVO.setChannel(channel);
            return reptileContentVO;
            //channel为2查询微信公众号
        } else if (channel == 2) {
            WeixinOfficialAccount weixinOfficialAccount = weixinOfficialAccountService.getById(id);
            BeanUtils.copyProperties(weixinOfficialAccount, reptileContentVO);
            reptileContentVO.setChannel(channel);
            return reptileContentVO;
        }
        return null;
    }
}