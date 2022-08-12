package com.sun.content.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author rebort
 * @description: TODO
 * @date 2022/2/23
 */
@Data
@ApiModel("爬虫分页查询参数")
public class ReptileContentQueryDTO {

    @ApiModelProperty(value = "关键字:可以模糊搜索标题，标签，文章，账号")
    private String keyword;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "粉丝数量")
    private Integer fansNumber;

    @ApiModelProperty(value = "排序")
    private Map<String, Boolean> sort;

    @ApiModelProperty(value = "渠道")
    private Integer channel;


}
