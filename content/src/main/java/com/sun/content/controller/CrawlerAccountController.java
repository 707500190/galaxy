import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.common.utils.DateUtil;
import com.sun.content.api.common.utils.R;
import com.sun.content.api.dto.CrawlerAccountDTO;
import com.sun.content.api.dto.CrawlerBaseDTO;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerStrategyAccount;
import com.sun.content.api.vo.CrawlerReportVO;
import com.sun.content.api.vo.SearchDataResult;
import com.sun.content.service.CrawlerAccountReportService;
import com.sun.content.service.CrawlerStrategyAccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.sun.content.util.CommonUtil.getOrderItemByMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sunshilong
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/crawler/account")
@RequiredArgsConstructor
public class CrawlerAccountController {

    private final CrawlerStrategyAccountService accountService;
    private final CrawlerAccountReportService reportService;

    /**
     * 账号详情
     *
     * @param id 主键
     * @return 账号详情
     */
    @ApiOperation(value = "获取单个账号")
    @GetMapping("detail")
    public R<CrawlerBaseDTO> getAccount(@RequestParam("id") Long id) {
        CrawlerStrategyAccount account = accountService.getById(id);
        String tags = account.getAccountTag();
        List<String> tagList = Arrays.asList(StringUtils.split(tags, ","));
        CrawlerBaseDTO dto = new CrawlerBaseDTO();
        dto.setAccountTag(tagList);
        BeanUtils.copyProperties(account, dto);
        return R.ok(dto);
    }

    /**
     * 根据策略获取账号列表
     *
     * @param dto 账号列表
     * @return 账号列表
     */
    @ApiOperation(value = "爬虫策略列表")
    @PostMapping("page")
    public R<IPage<CrawlerStrategyAccount>> list(@RequestBody CrawlerReportDTO dto) {
        Page<CrawlerStrategyAccount> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getPageSize());
        page.setPages(dto.getPageSize());
        QueryWrapper<CrawlerStrategyAccount> wrapper = new QueryWrapper<>();
        if (dto.getId() != null) {
            wrapper.eq("strategy_id", dto.getId());
        }
        if (dto.getChannel() != null) {
            wrapper.eq("channel", dto.getChannel());
        }
        page.setOrders(getOrderItemByMap(dto.getSort()));
        IPage<CrawlerStrategyAccount> list = accountService.page(page, wrapper);
        return R.ok(list);
    }

    /**
     * 账号删除
     *
     * @param id 主键
     * @return 账号删除
     */
    @ApiOperation(value = "账号删除")
    @GetMapping("delete")
    public R<String> deleteAccount(@RequestParam("id") Long id) {
        accountService.removeById(id);
        return R.ok("success");
    }

    /**
     * 账号更新
     *
     * @return 账号更新
     */
    @ApiOperation(value = "账号更新")
    @PostMapping("update")
    public R<String> updateAccount(@RequestBody CrawlerAccountDTO dto) {
        accountService.saveAccount(dto);
        return R.ok("success");
    }

    @ApiOperation(value = "账号报表")
    @PostMapping("report")
    public R<SearchDataResult<CrawlerReportVO>> listReport(@RequestBody CrawlerReportDTO dto) {
        Date now = new Date();
        //上个月到现在；
        dto.setMonthStart(DateUtil.getDateOfLastMonth());
        dto.setMonthEnd(now);
        //去年到现在；
        dto.setYearStart(DateUtil.getDayOfLastYear());
        dto.setYearEnd(now);
        SearchDataResult<CrawlerReportVO> list = reportService.pageReport(dto);
        return R.ok(list);
    }

    @ApiOperation(value = "同步报表")
    @GetMapping("sync/report")
    public R<String> syncReport() {
        CrawlerReportDTO dto = new CrawlerReportDTO();
        Date now = new Date();
        //上个月到现在；
        dto.setMonthStart(DateUtil.getDateOfLastMonth());
        dto.setMonthEnd(now);
        //去年到现在；
        dto.setYearStart(DateUtil.getDayOfLastYear());
        dto.setYearEnd(now);
        accountService.syncAccountReport(dto);
        return R.ok();
    }

}

