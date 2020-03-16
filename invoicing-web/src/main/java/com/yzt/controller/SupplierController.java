package com.yzt.controller;

import com.yzt.entity.Supplier;
import com.yzt.service.SupplierService;
import com.yzt.util.ExcelUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @RequestMapping(value = "toSupplier", method = RequestMethod.GET)
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

    @RequestMapping(value = "toSupplier", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updSupplier(@RequestBody Supplier supplier) {
        try {
            supplierService.updSupplier(supplier);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toSupplier", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> insSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "toSupplier", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delSupplier(@RequestBody Integer supplierID) {
        System.out.println(supplierID);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "supplierInfo", method = RequestMethod.GET)
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

    @RequestMapping(value = "toSupplier/{companyName}", method = RequestMethod.GET)
    public ResponseEntity<List<Supplier>> getByName(@PathVariable String companyName) {
        try {
            List<Supplier> supplier = supplierService.selByCompanyName(companyName);
            return ResponseEntity.ok(supplier);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @RequestMapping(value = "toSupplier/id/{supplierID}", method = RequestMethod.GET)
    public ResponseEntity<Supplier> getByID(@PathVariable Integer supplierID) {
        try {
            Supplier suppliers = supplierService.selBySupplierID(supplierID);
            if (suppliers == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(suppliers);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toSupplier/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportExcel(HttpServletRequest req, HttpServletResponse resp) {
        String companyName = req.getParameter("companyName");
        List<Supplier> list = supplierService.selByCompanyName(companyName);
        String fileName = UUID.randomUUID() + ".xlsx";
        String[] title = {"供应商编号", "公司名称", "联系人", "联系人职位", "地址", "城市", "电话", "传真", "邮政编码"};
        String sheetName = "供应商信息表";
        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Supplier supplier = list.get(i);
            content[i][0] = supplier.getSupplierID();
            content[i][1] = supplier.getCompanyName();
            content[i][2] = supplier.getContactName();
            content[i][3] = supplier.getContactTitle();
            content[i][4] = supplier.getAddress();
            content[i][5] = supplier.getCity();
            content[i][6] = supplier.getPhone();
            content[i][7] = supplier.getFax();
            content[i][8] = supplier.getPostalCode();
        }
        ExcelUtil excelUtil = new ExcelUtil();
        XSSFWorkbook workbook = excelUtil.getHSSFWorkbook(sheetName, title, content, null, null, null);
        try {
            this.setResponseHeader(resp, fileName);
            System.out.println("hello");
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
