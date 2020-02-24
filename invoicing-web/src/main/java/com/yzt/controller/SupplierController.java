package com.yzt.controller;

import com.yzt.entity.Supplier;
import com.yzt.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
    /*
        count : 数据总条数
        totalPage : 总页数
        pageSize : 每个页面的大小
    */
    @Resource
    private SupplierService supplierService;

    private int count;
    private int totalPage;
    private int pageSize = 10;

    @RequestMapping("toSupplier")
    public String toSupplier(Model model) {
        List<Supplier> suppliers = supplierService.allSupplier(0, pageSize);
        count = supplierService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/purchase/supplier";
    }

    @RequestMapping("supplierInfo")
    public String supplierInfo(HttpServletRequest req, Model model) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Supplier> suppliers = supplierService.allSupplier(pageStart, pageSize);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/purchase/supplier";
    }

}
