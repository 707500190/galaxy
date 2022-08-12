package com.sun.content.template;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sun.content.api.dto.QueryContentDTO;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/24
 */
public interface ContentTemplateService<T> {


    List<QueryContentDTO> getContentList(Wrapper<T> wrapper);

    List<QueryContentDTO> handle(List<T> entity);
}
