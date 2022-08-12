package com.sun.content.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/26
 */
public class CommonUtil {

    public static List<OrderItem> getOrderItemByMap(Map<String, Boolean> map) {
        List<OrderItem> res = Lists.newArrayList();
        if (CollectionUtils.isEmpty(map)) {
            return res;
        }
        OrderItem item;
        for (String key : map.keySet()) {
            item = new OrderItem();
            item.setColumn(key);
            item.setAsc(map.get(key));
            res.add(item);
        }
        return res;
    }
}
