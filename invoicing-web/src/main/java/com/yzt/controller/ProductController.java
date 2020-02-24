package com.yzt.controller;

import com.yzt.entity.Product;
import com.yzt.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    /*
        count : 数据总条数
        totalPage : 总页数
        pageSize : 每个页面的大小
     */

    @Resource
    private ProductService productService;

    private int count;
    private int totalPage;
    private int pageSize = 10;

    @RequestMapping("toProductMessage")
    public String toProductMessage(Model model) {
        List<Product> products = productService.allProduct(0, pageSize);
        count = productService.dataCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        model.addAttribute("products", products);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/product/productMessage";
    }

    @RequestMapping("add")
    public String toAdd() {
        return "/product/addProduct";
    }

    @RequestMapping("productInfo")
    public String productInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Product> products = productService.allProduct(pageStart, pageSize);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/product/productMessage";
    }

    @RequestMapping(value = "{productID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Product> findProduct(@PathVariable("productID") Integer productID) {
        Product product = productService.selByID(productID);
        return ResponseEntity.ok(product);
    }


}
