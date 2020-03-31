package com.yzt.controller;

import com.yzt.entity.UserList;
import com.yzt.service.SystemService;
import com.yzt.util.DataBaseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        List<UserList> userList = systemService.selAllUser(0, pageSize);
        count = systemService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        return "/system/userManage";
    }

    @RequestMapping(value = "userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<UserList> userList = systemService.selAllUser(pageStart, pageSize);
        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/system/userManage";
    }


    @RequestMapping(value = "toUserManage/{employeeID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> getPermission(@PathVariable Integer employeeID) {
        try {
            if (employeeID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<String> userList = systemService.selByEmployeeID(employeeID);
            if (userList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(userList);
        }catch (Exception e) {

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
            if (employeeID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            systemService.delUser(employeeID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "backup", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Void> backup(HttpServletResponse resp) {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = sdf.format(date) + ".sql";
            this.setResponseHeader(resp, fileName);
            File backup = DataBaseUtil.backup(fileName);
            Thread.sleep(3000);
            FileInputStream fis = new FileInputStream(backup);
            OutputStream os = resp.getOutputStream();
            int len;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) > 0) {
                os.write(bytes, 0, len);
            }
            fis.close();
            os.flush();
            os.close();
            return ResponseEntity.ok(null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "recover", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> recover(HttpServletRequest req) {
        try {
            MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) req;
            MultipartFile file = fileRequest.getFile("file");
            DataBaseUtil.dbRestore(file);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
