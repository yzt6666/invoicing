package com.yzt.service.com.yzt.service.impl;

import com.yzt.mapper.AnalysisMapper;
import com.yzt.service.AnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Resource
    private AnalysisMapper analysisMapper;

    @Override
    public List<Map> selUnitsOnOrder(String year, String month, String flag) {
        return analysisMapper.selUnitsOnOrder(year, month, flag);
    }

    @Override
    public List<Map> selCategorySale(String year, String month, Integer categoryID, String flag) {
        return analysisMapper.selCategorySale(year, month, categoryID, flag);
    }

    @Override
    public List<Map> selYear() {
        return analysisMapper.selYear();
    }

    @Override
    public List<Map> selProductRank(String year, String month, String flag) {
        return analysisMapper.selProductRank(year, month, flag);
    }

    @Override
    public List<Map> selPay(String year) {
        return analysisMapper.selPay(year);
    }

    @Override
    public List<Map> selProfit(String year) {
        return analysisMapper.selProfit(year);
    }
}
