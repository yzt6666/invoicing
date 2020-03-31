package com.yzt.controller;

import com.yzt.entity.*;
import com.yzt.service.PurchaseService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;

    private int count;
    private int totalPage;
    private int pageSize = 10;

    @RequestMapping(value = "purchaseOrder", method = RequestMethod.GET)
    public String toPurchaseOrder(Model model) {
        List<Map> purchaseOrders = purchaseService.selPurchaseOrder(0, pageSize);
        count = purchaseService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        CommonUtil.dateConvert(purchaseOrders);
        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/purchase/purchaseOrder";
    }

    @RequestMapping(value = "orderInfo", method = RequestMethod.GET)
    public String orderInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> purchaseOrders = purchaseService.selPurchaseOrder(pageStart, pageSize);
        CommonUtil.dateConvert(purchaseOrders);
        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/purchase/purchaseOrder";
    }

    @RequestMapping(value = "purchaseOrder/create", method = RequestMethod.GET)
    public String toAdd(Model model) {
        List<Category> categories = purchaseService.selCategory();
        List<Product> productInfo = purchaseService.selByCategoryID(1);
        List<Supplier> suppliers = purchaseService.selSupplier();
        model.addAttribute("categories", categories);
        model.addAttribute("products", productInfo);
        model.addAttribute("suppliers", suppliers);
        return "/purchase/addOrder";
    }

    @RequestMapping(value = "purchaseOrder/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> addOrder(@RequestBody List<Map<String, String>> list) {
        try {
            if (list == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Subject subject = SecurityUtils.getSubject();
            UserList user = (UserList) subject.getPrincipal();
            purchaseService.insPurchaseOrder(list, user.getEmployeeID());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "purchaseOrder/{categoryID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Product>> selByCategoryID(@PathVariable("categoryID") Integer categoryID) {
        try {
            if (categoryID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Product> products = purchaseService.selByCategoryID(categoryID);
            return ResponseEntity.ok(products);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "purchaseOrder/detail/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> selOrderDetail(@PathVariable("orderID") String orderID) {
        try {
            if (StringUtils.isEmpty(orderID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> maps = purchaseService.selByOrderID(orderID);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "purchaseOrder/order", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> selOrder(HttpServletRequest req) {
        try {
            String flag = req.getParameter("flag");
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            List<Map> maps = purchaseService.selOrderByFlag(flag, startDate, endDate);
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
    @RequestMapping(value = "purchaseOrder/order", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> returnOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            purchaseService.updOrder(orderID);
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
    @RequestMapping(value = "purchaseOrder/order", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            purchaseService.delOrder(orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "purchaseOrder/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportExcel(HttpServletRequest req, HttpServletResponse resp) {
        String orderID = req.getParameter("orderID");
        List<Map> list = purchaseService.selOrderByID(orderID);
        CommonUtil.dateConvert(list);
        String fileName = UUID.randomUUID() + ".xlsx";
        String[] title = {"订单编号", "供应商名称", "采购员", "下单日期", "联系人", "联系电话", "产品名称", "数量", "合计金额","备注"};
        String sheetName = "采购订单表";
        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            content[i][0] = String.valueOf(map.get("orderID"));
            content[i][1] = String.valueOf(map.get("companyName"));
            content[i][2] = String.valueOf(map.get("username"));
            content[i][3] = String.valueOf(map.get("orderDate"));
            content[i][4] = String.valueOf(map.get("contactName"));
            content[i][5] = String.valueOf(map.get("phone"));
            content[i][6] = String.valueOf(map.get("productName"));
            content[i][7] = String.valueOf(map.get("quantity"));
            content[i][8] = String.valueOf(map.get("totalPrice"));
            content[i][9] = String.valueOf(map.get("remark"));
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
