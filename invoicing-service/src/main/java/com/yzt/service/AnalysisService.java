package com.yzt.service;

import java.util.List;
import java.util.Map;

public interface AnalysisService {
    List<Map> selUnitsOnOrder(String year, String month, String flag);

    List<Map> selCategorySale(String year, String month, Integer categoryID, String flag);

    List<Map> selYear();

    List<Map> selProductRank(String year, String month, String flag);

    List<Map> selPay(String year);

    List<Map> selProfit(String year);
}
