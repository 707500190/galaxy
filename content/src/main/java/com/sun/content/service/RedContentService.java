package com.sun.content.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.dto.ReptileContentQueryDTO;
import com.sun.content.api.entity.RedContent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.vo.ReptileContentVO;
import com.sun.content.api.vo.ReptilePageVO;


/**
 * @描述：小红书主体 服务类
 * @作者: tw
 * @日期: 2022-02-23
 */
public interface RedContentService extends IService<RedContent> {

    IPage<ReptilePageVO> page(Page page, ReptileContentQueryDTO reptileContentQueryDTO);

    ReptileContentVO getDetailById(Integer channel, Long id);
}