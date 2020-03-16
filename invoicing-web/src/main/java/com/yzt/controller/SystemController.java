package com.yzt.controller;

import com.yzt.entity.Customer;
import com.yzt.entity.UserList;
import com.yzt.service.SystemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController {
    @Resource
    private SystemService systemService;

    private int count;
    private int totalPage;
    private int pageSize = 20;

    @RequestMapping(value = "toUserManage", method = RequestMethod.GET)
    public String toUserManage(Model model) {
        List<UserList> userlist = systemService.selAllUser(0, pageSize);
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        model.addAttribute("userlist", userlist);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        return "/system/userManage";
    }

    @RequestMapping(value = "toUserManage/{employeeID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> getPermission(@PathVariable Integer employeeID) {
        List<String> userList = systemService.selByEmployeeID(employeeID);
        if (userList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(userList);
    }

    @RequestMapping(value = "toUserManage", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updPermission(@RequestBody List<String> list) {
        try {
            systemService.updPerms(list);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toUserManage", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> delUser(@RequestBody Integer employeeID) {
        try {
            if (employeeID.intValue() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            System.out.println(employeeID);
            systemService.delUser(employeeID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping("toBackUp")
    public String toBackUp() {
        return "/system/backup";
    }
}
