package com.yzt.controller;

import com.yzt.entity.*;
import com.yzt.service.PurchaseService;
import com.yzt.service.SaleService;
import com.yzt.util.CommonUtil;
import com.yzt.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/sale")
public class SaleController {
    @Resource
    private SaleService saleService;
    @Resource
    private PurchaseService purchaseService;

    private int count;
    private int totalPage;
    private int pageSize = 50;

    @RequestMapping(value = "saleOrder", method = RequestMethod.GET)
    public String toSaleOrder(Model model) {
        List<Map> saleOrders = saleService.selSaleOrder(0, pageSize);
        count = saleService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        CommonUtil.dateConvert(saleOrders);
        model.addAttribute("saleOrders", saleOrders);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/saleOrder";
    }

    @RequestMapping(value = "saleOrder/create", method = RequestMethod.GET)
    public String toAddOrder(Model model) {
        List<Category> categories = purchaseService.selCategory();
        List<Product> productInfo = purchaseService.selByCategoryID(1);
        List<Customer> customers = saleService.selCustomer();
        model.addAttribute("categories", categories);
        model.addAttribute("products", productInfo);
        model.addAttribute("customers", customers);
        return "/sale/addSaleOrder";
    }

    @RequestMapping(value = "saleOrder/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> addOrder(@RequestBody List<Map<String, String>> list) {
        try {
            if (list == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Subject subject = SecurityUtils.getSubject();
            UserList user = (UserList) subject.getPrincipal();
            saleService.insSaleOrder(list, user.getEmployeeID());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "orderInfo", method = RequestMethod.GET)
    public String orderInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> saleOrders = saleService.selSaleOrder(pageStart, pageSize);
        CommonUtil.dateConvert(saleOrders);
        model.addAttribute("saleOrders", saleOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/saleOrder";
    }

    @RequestMapping(value = "saleOrder/detail/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> saleDetail(@PathVariable("orderID") String orderID){
        try {
            if (StringUtils.isEmpty(orderID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> maps = saleService.selSaleOrderDetail(orderID);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "saleOrder/order", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> selOrder(HttpServletRequest req) {
        try {
            String flag = req.getParameter("flag");
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            List<Map> maps = saleService.selOrderByFlag(flag, startDate, endDate);
            CommonUtil.dateConvert(maps);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 退单
     * @param map
     * @return
     */
    @RequestMapping(value = "saleOrder/order", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> returnOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            saleService.updOrder(orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除订单
     * @param map
     * @return
     */
    @RequestMapping(value = "saleOrder/order", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> delOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            saleService.delOrder(orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "saleOrder/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportExcel(HttpServletRequest req, HttpServletResponse resp) {
        String orderID = req.getParameter("orderID");
        List<Map> list = saleService.selOrderByID(orderID);
        CommonUtil.dateConvert(list);
        String fileName = UUID.randomUUID() + ".xlsx";
        String[] title = {"订单编号", "客户名称", "销售员", "下单日期", "收货人", "详细地址", "产品名称", "数量", "合计金额"};
        String sheetName = "销售订单表";
        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            content[i][0] = String.valueOf(map.get("orderID"));
            content[i][1] = String.valueOf(map.get("companyName"));
            content[i][2] = String.valueOf(map.get("username"));
            content[i][3] = String.valueOf(map.get("orderDate"));
            content[i][4] = String.valueOf(map.get("shipName"));
            content[i][5] = String.valueOf(map.get("shipCity")) + map.get("shipAddress");
            content[i][6] = String.valueOf(map.get("productName"));
            content[i][7] = String.valueOf(map.get("quantity"));
            content[i][8] = String.valueOf(map.get("totalPrice"));
        }
        ExcelUtil excelUtil = new ExcelUtil();
        XSSFWorkbook workbook = excelUtil.getHSSFWorkbook(sheetName, title, content, null, null, null);
        try {
            this.setResponseHeader(resp, fileName);
            OutputStream os = resp.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
