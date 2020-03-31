package com.yzt.controller;

import com.yzt.entity.Customer;
import com.yzt.entity.Supplier;
import com.yzt.service.CustomerService;
import com.yzt.util.ExcelUtil;
import org.apache.catalina.startup.Catalina;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @RequestMapping(value = "toCustomer", method = RequestMethod.GET)
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

    @RequestMapping(value = "toCustomer", method = RequestMethod.POST)
    public ResponseEntity<Void> insCustomer(@RequestBody Customer customer) {
        try {
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            customerService.insCustomer(customer);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toCustomer", method = RequestMethod.PUT)
    public ResponseEntity<Void> updCustomer(@RequestBody Customer customer) {
        try {
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            customerService.updCustomer(customer);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toCustomer", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delCustomer(@RequestBody Map<String, String> map) {
        String customerID = map.get("customerID");
        try {
            if (customerID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            customerService.delCustomer(customerID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toCustomer/{customerID}", method = RequestMethod.GET)
    public ResponseEntity<Customer> selByCustomerID(@PathVariable String customerID) {
        try {
            Customer customer = customerService.selByCustomerID(customerID);
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(customer);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toCustomer/name/{companyName}", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> selByCompanyName(@PathVariable String companyName) {
        try {
            List<Customer> customers = customerService.selByCompanyName(companyName);
            if (customers == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(customers);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "customerInfo", method = RequestMethod.GET)
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

    @RequestMapping(value = "toCustomer/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportExcel(HttpServletRequest req, HttpServletResponse resp) {
        String companyNames = req.getParameter("companyName");
        List<Customer> list;
        if (companyNames == "") {
            list = customerService.selAllCustomer();
        } else {
            String[] nameArray = companyNames.split(",");
            List<String> res = new ArrayList<>();
            for (String str : nameArray) {
                res.add(str);
            }
            list = customerService.selCustomerByNames(res);
        }
        System.out.println(list);
        String fileName = UUID.randomUUID() + ".xlsx";
        String[] title = {"客户编号", "公司名称", "联系人", "联系人职位", "地址", "城市", "电话", "传真", "邮政编码"};
        String sheetName = "客户信息表";
        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            Customer customer = list.get(i);
            content[i][0] = customer.getCustomerID();
            content[i][1] = customer.getCompanyName();
            content[i][2] = customer.getContactName();
            content[i][3] = customer.getContactTitle();
            content[i][4] = customer.getAddress();
            content[i][5] = customer.getCity();
            content[i][6] = customer.getPhone();
            content[i][7] = customer.getFax();
            content[i][8] = customer.getPostalCode();
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
