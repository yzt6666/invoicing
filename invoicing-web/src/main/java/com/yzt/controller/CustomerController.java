package com.yzt.controller;

import com.yzt.entity.Customer;
import com.yzt.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    /*
        count : 数据总条数
        totalPage : 总页数
        pageSize : 每个页面的大小
     */
    @Resource
    private CustomerService customerService;

    private int count;
    private int totalPage;
    private int pageSize = 20;

    @RequestMapping("toCustomer")
    public String toCustomer(Model model) {
        List<Customer> customers = customerService.allCustomer(0, pageSize);
        count = customerService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/customer";
    }

    @RequestMapping("customerInfo")
    public String customerInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Customer> customers = customerService.allCustomer(pageStart, pageSize);
        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/customer";
    }
}
