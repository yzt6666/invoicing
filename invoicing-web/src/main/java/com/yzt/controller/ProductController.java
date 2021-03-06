package com.yzt.controller;

import com.yzt.entity.Category;
import com.yzt.entity.Product;
import com.yzt.entity.Supplier;
import com.yzt.service.ProductService;
import com.yzt.service.PurchaseService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private PurchaseService purchaseService;

    private int count;
    private int totalPage;
    private int pageSize = 10;
    private static final String rootPath = "D:\\IDEA2019\\invoicing\\invoicing-web\\src\\main\\resources\\static\\img\\";

    @RequestMapping(value = "toProductMessage", method = RequestMethod.GET)
    public String toProductMessage(Model model) {
        List<Map> products = productService.allProduct(0, pageSize);
        count = productService.dataCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        List<Category> categories = purchaseService.selCategory();
        List<Supplier> suppliers = purchaseService.selSupplier();
        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/product/productMessage";
    }

    @RequestMapping(value = "toProductMessage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> insProduct(@RequestBody Product product) {
        try {
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            productService.insProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toProductMessage", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updProduct(@RequestBody Product product) {
        try {
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            productService.updProduct(product);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toProductMessage/addImg", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> insImg(HttpServletRequest req) {
        try {
            Thread.sleep(1000);
            String productName = req.getParameter("productName");
            MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) req;
            MultipartFile file = fileRequest.getFile("file");
            String imgPath = rootPath + file.getOriginalFilename();
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imgPath));
            productService.updImg(productName, file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toProductMessage/img", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getImg(Integer productID) {
        try {
            if (productID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            System.out.println(productID);
            String imgPath = productService.selImgPath(productID);
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("imgPath", imgPath);
            list.add(map);
            System.out.println(list);
            return ResponseEntity.status(HttpStatus.OK).body(imgPath);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toProductMessage", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> delProduct(@RequestBody Map<String, Integer> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Integer productID = map.get("productID");
            productService.delProduct(productID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    @RequestMapping(value = "productInfo", method = RequestMethod.GET)
    public String productInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> products = productService.allProduct(pageStart, pageSize);
        List<Category> categories = purchaseService.selCategory();
        List<Supplier> suppliers = purchaseService.selSupplier();
        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/product/productMessage";
    }

    @RequestMapping(value = "toProductMessage/{productID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> findProduct(@PathVariable("productID") Integer productID) {
        try {
            if (productID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> map = productService.selByID(productID);
            return ResponseEntity.ok(map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toProductMessage/name/{productName}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> findByName(@PathVariable String productName) {
        try {
            if (productName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> maps = productService.selByProductName(productName);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
