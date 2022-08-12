package com.sun.content.controller;


import com.sun.content.service.SearchBehaviorService;
import com.sun.content.api.common.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sunshilong
 * @since 2022-05-07
 */
@RequestMapping("/behavior")
@RestController

@RequiredArgsConstructor
public class SearchBehaviorController {

    private final SearchBehaviorService searchBehaviorService;

    /**
     * 热门历史
     *
     * @return 结果集
     */
    @ApiOperation(value = "热门历史")
    @GetMapping("hot")
    public R getHotHistory() {
        return R.ok(searchBehaviorService.getKeywordRank());
    }

    /**
     * 热门历史
     *
     * @return 结果集
     */
    @ApiOperation(value = "热门历史")
    @GetMapping("history")
    public R getHotHistory(@RequestParam String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return R.ok("");
        }
        return R.ok(searchBehaviorService.getHistoryByKeyword(keyword));
    }

}

