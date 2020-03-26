package com.yzt.util;

import java.util.List;
import java.util.Map;

public class CommonUtil {
    //解决时间精确到毫秒的情况
    public static void dateConvert(List<Map> saleOrders) {
        for (int i = 0; i < saleOrders.size(); i++) {
            Map map = saleOrders.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
    }
}
