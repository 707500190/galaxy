package com.sun.content.search;

import com.sun.content.api.dto.Pair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
@Slf4j
@Data
public class SearchOption {

    @ApiModelProperty(value = "关键字:可以模糊搜索标题，标签，文章，账号")
    private String keyword;

    private Filter filter;

    @ApiModelProperty(value = "下拉搜索 1：是   0： 否")
    private Integer dropdown;

    @ApiModelProperty(value = "索引名称")
    private String index;

    private Integer page = 1;

    private Integer pageSize = 10;


    /**
     * true 是升序  false 是降序
     * <Field, Boolean>
     */
    @ApiModelProperty(value = "排序")
    private Map<String, Boolean> sort;


    /**
     * filter查询：不参与打分计算，查询速度偏快
     */
    @Data
    public static class Filter {

        private String id;

        @ApiModelProperty(value = "渠道")
        private String channel;

        @ApiModelProperty(value = "粉丝数量")
        private Pair<Integer> fansNumber;

        @ApiModelProperty(value = "发布时间区间")
        private Pair<Date> publishTime;

    }
}
