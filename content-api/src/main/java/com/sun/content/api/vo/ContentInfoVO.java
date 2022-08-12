package com.sun.content.api.vo;

import com.sun.content.api.entity.ContentInfo;
import lombok.Data;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/9
 */
@Data
public class ContentInfoVO extends ContentInfo {

    private List<String> articleContentList;
}
