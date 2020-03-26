package com.yzt.controller;

import com.yzt.entity.Customer;
import com.yzt.service.CustomerService;
import org.apache.catalina.startup.Catalina;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
}
